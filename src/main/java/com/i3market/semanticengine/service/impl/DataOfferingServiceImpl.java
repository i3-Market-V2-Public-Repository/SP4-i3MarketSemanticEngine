package com.i3market.semanticengine.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.GsonBuilder;
import com.i3market.semanticengine.cache.DataOfferingCache;
import com.i3market.semanticengine.cache.NodesCache;
import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.entity.*;
import com.i3market.semanticengine.common.domain.request.*;
import com.i3market.semanticengine.common.domain.response.*;

import com.i3market.semanticengine.exception.InvalidInputException;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.mapper.Mapper;
import com.i3market.semanticengine.repository.DataOfferingRepository;
import com.i3market.semanticengine.repository.DataProviderRepository;
import com.i3market.semanticengine.service.DataOfferingService;
import eu.i3market.seedsindex.DataCategory;
import eu.i3market.seedsindex.SearchEngineIndexRecord;
import eu.i3market.seedsindex.SeedsIndex;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataOfferingServiceImpl implements DataOfferingService {

//    @Value("${category-list.url}")
//    private String categoryListUrl;

    private final DataOfferingRepository dataOfferingRepository;

    private final DataProviderRepository dataProviderRepository;

    private final Mapper mapper;

    private final WebClient.Builder webClient;

    @Autowired
    private DataOfferingCache dataOfferingCache;

    @Autowired
    private NodesCache nodesCache;


    @Override
    public Flux<CategoriesList> getCategoryList(ServerHttpRequest serverHttpRequest) {

//        return webClient.build().get().uri(categoryListUrl).retrieve()
//                .bodyToFlux(CategoriesList.class)
//                .log(log.getName(), Level.FINE);

        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.251:8545",
                "0xdc46ccc4e79eb75c3eaeb2419e5582f7d86f0212a459886be787e00ca1edb4ae");
        ArrayList<CategoriesList> categoriesLists = new ArrayList<>();
        try{
            seedsIndex.init();
            final Collection<SearchEngineIndexRecord> byDataCategory = seedsIndex.findByDataCategory(null);
            final Iterator<SearchEngineIndexRecord> iterator = byDataCategory.iterator();
            while(iterator.hasNext()){
                Arrays.stream(iterator.next().getCategories()).forEach(e-> categoriesLists
                        .add(new CategoriesList((e.name().substring(0,1) + e.name().substring(1).toLowerCase()),e.description())));
            }
            categoriesLists.sort(comparingCategoriesList);

            return Flux.fromIterable(categoriesLists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            seedsIndex.shutdown();
        }
        return null;
    }

    @Override
    @SneakyThrows
    public Mono<DataOfferingDto> createDataOffering(final RequestDataOffering dto, ServerHttpRequest serverHttpRequest) {

//        final Flux<CategoriesList> log = webClient.build().get().uri("http://95.211.3.244:7500/data_categories").retrieve()
//                .bodyToFlux(CategoriesList.class)
//                .log(DataOfferingServiceImpl.log.getName(), Level.FINE);

        System.out.println("Category List");
//        log.subscribe(e-> System.out.println(e));
      //  System.out.println(serverHttpRequest.getURI());
        final Mono<URI> just = Mono.just(serverHttpRequest.getURI());
       just.subscribe(e->System.out.println("Mono   "+e));
        final String substring = String.valueOf(serverHttpRequest.getURI()).substring(0, 19);
        System.out.println("URI  "+substring);

        final var entity = mapper.requestToEntity(dto);
        entity.setCreatedAt(Instant.now());

        final var newEntity = dataOfferingRepository.save(entity);

        OkHttpClient client = new OkHttpClient();

        //this is a documentation test
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),"{\n" +
                "  \"message\": {\"name\":\""+ entity +"\"},\n" +
                "  \"receiver_id\": \"offering.new\"\n" +
                "}");


        String val = null;
        try {
            final Request build = new Request.Builder()
                    .url(substring+":3000/notification-manager-oas/api/v1/notification/service")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(build).execute();
            val = response.body().string();
            System.out.println(val);
            System.out.println(response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            return newEntity.log(DataOfferingServiceImpl.log.getName(), Level.FINE)
                    .onErrorMap(RuntimeException.class,
                            err -> new InvalidInputException(HttpStatus.BAD_REQUEST, "Error"))
                    .map(mapper::entityToDto);
        }
    }

    @Override
    public Mono<Void> deleteAllOffering() {
        return dataOfferingRepository.deleteAll();
    }

    @Override
    public Mono<DataOfferingDto> getDataOfferingById(final String id) {
        final var entity = dataOfferingRepository.findById(id);
        return entity.log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found for id : " + id)))
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<OfferingIdResponse> getOfferingList(final int page, final int size) {

        return dataOfferingRepository.findAll()
                .log(log.getName(), Level.FINE)
                .map(mapper::entityToDto)
                .skip(page * size).take(size)
                .sort(compareOfferingTime.reversed())
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found.")))
                .map(e -> OfferingIdResponse.builder().offering(e.getDataOfferingId()).build());
    }
    @Override
    public Flux<OfferingIdResponse> getOfferingListonActive(final int page, final int size) {
        return dataOfferingRepository.findAll()
                .log(log.getName(), Level.FINE)
                .map(mapper::entityToDto)
                .filter(e-> e.isActive() == true)
                .skip(page * size).take(size)
                .sort(compareOfferingTime.reversed())
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found.")))
                .map(e -> OfferingIdResponse.builder().offering(e.getDataOfferingId()).build());
    }

    @Override
    public Flux<OfferingIdResponse> getOfferingListonSharedNetwork(final int page, final int size) {
        return dataOfferingRepository.findAll()
                .log(log.getName(), Level.FINE)
                .map(mapper::entityToDto)
                .filter(e-> e.isInSharedNetwork()==true)
                .skip(page * size).take(size)
                .sort(compareOfferingTime.reversed())
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No data offering found.")))
                .map(e -> OfferingIdResponse.builder().offering(e.getDataOfferingId()).build());
    }

    @Override
    public Mono<DataOfferingDto> updateDataOffering(final DataOfferingDto body) {
        dataOfferingCache.clear(body.getDataOfferingId());
        final var currentEntity = dataOfferingRepository.findById(body.getDataOfferingId());
        final var updateEntity = mapper.dtoToEntity(body);
        return currentEntity.log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! Data Offering id" + body.getDataOfferingId() + " does not exist")))
                .map(e -> offeringTransform(updateEntity, e).toBuilder().updatedAt(Instant.now()).build())
                .flatMap(dataOfferingRepository::save)
                .map(mapper::entityToDto);
    }

//    public Mono<DataOfferingDto> updateDataOffering(final DataOfferingDto body){
//         DataOffering dataOffering = mapper.dtoToEntity(body);
//        System.out.println("\n data offering two \n "+body.toString());
//        System.out.println("\n data offering one \n "+dataOffering.toString());
////        final Mono<DataOffering> byId = dataOfferingRepository.findById(body.getDataOfferingId());
////        byId.subscribe(e-> System.out.println("data offering \n"+e.toString()));
//
//        return null;
//    }

    @Override
    public Flux<DataOfferingDto> getOfferingByProvider(final String provider, final int page, final int size) {
        return dataOfferingRepository.findByProvider(provider.toLowerCase())
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for provider: " + provider)));
    }

    @Override
    public Flux<DataOfferingDto> getOfferingByCategory(final String category, final int page, final int size) {
        return dataOfferingRepository.findByCategory(category.strip().toLowerCase())
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for category: " + category)));
    }

    @Override
    public Mono<ContractParametersResponse> getContractParameterByOfferingId(final String offeringId) {
        return dataOfferingRepository.findById(offeringId)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "We are sorry! No contract parameter found:")))
                .map(mapper::contractParameterDto);
    }

    @Override
    public Flux<ProviderIdResponse> getProviderListByCategory(final String category, final int page, final int size) {
        return dataOfferingRepository.findByCategory(category).skip(page * size).take(size).map(mapper::entityToDto)
                .sort(compareByProvider).map(mapper::providerIdDto);
    }

    @Override
    @SneakyThrows
    public Mono<TotalOfferingResponse> getOfferingByProviderIdAndCategorySorted(final String inputProviderId, final String inputCategory,
                                                                                final int page, final int size, final String sortBy, final String orderIn) {
        final String providerId = inputProviderId.strip();
        final String category = inputCategory.strip().toLowerCase();
        if (providerId.isEmpty() || providerId.isBlank() || providerId.length() == 0) {
            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "You have inputted an invalid providerId");
        } else if (category.isBlank() || category.isEmpty() || category.length() == 0) {
            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "You have inputted an invalid category");
        }

        final Flux<DataOfferingDto> dataOffering = dataOfferingRepository.findAll().map(mapper::entityToDto);

        List<DataOfferingDto> result = new ArrayList<>();
        int total = 0;

        if (providerId.equalsIgnoreCase("all")) {
            if (category.strip().equalsIgnoreCase("all")) {
                total = dataOffering.collectList().toFuture().get().size();
                if (sortBy.strip().equalsIgnoreCase("time")) {
                    if (orderIn.strip().equalsIgnoreCase("desc")) {
                        result.addAll(dataOffering.sort(compareOfferingTime.reversed()).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    } else {
                        result.addAll(dataOffering.sort(compareOfferingTime).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    }
                }
            } else {
                Flux<DataOfferingDto> offeringWithCategory = dataOffering.filter(e -> e.getCategory().equalsIgnoreCase(category.strip()));
                total = offeringWithCategory.collectList().toFuture().get().size();
                if (sortBy.strip().equalsIgnoreCase("time")) {
                    if (orderIn.strip().equalsIgnoreCase("desc")) {
                        result.addAll(offeringWithCategory.sort(compareOfferingTime.reversed()).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    } else {
                        result.addAll(offeringWithCategory.sort(compareOfferingTime).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    }
                }
            }
        } else {
            Flux<DataOfferingDto> offeringWithProviderId = dataOffering.filter(e -> e.getProvider().equalsIgnoreCase(providerId.strip().toLowerCase()));
            if (category.strip().equalsIgnoreCase("all")) {
                total = offeringWithProviderId.collectList().toFuture().get().size();
                if (sortBy.strip().equalsIgnoreCase("time")) {
                    if (orderIn.strip().equalsIgnoreCase("desc")) {
                        result.addAll(offeringWithProviderId.sort(compareOfferingTime.reversed()).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    } else {
                        result.addAll(offeringWithProviderId.sort(compareOfferingTime).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    }
                }
            } else {
                Flux<DataOfferingDto> offeringWithCategory = offeringWithProviderId.filter(e -> e.getCategory().equalsIgnoreCase(category.strip()));
                total = offeringWithCategory.collectList().toFuture().get().size();
                if (sortBy.strip().equalsIgnoreCase("time")) {
                    if (orderIn.strip().equalsIgnoreCase("desc")) {
                        result.addAll(offeringWithCategory.sort(compareOfferingTime.reversed()).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    } else {
                        result.addAll(offeringWithCategory.sort(compareOfferingTime).skip(page * size).take(size).sort(compareOfferingTitle).collectList().toFuture().get());
                    }
                }
            }

        }

        return Mono.just(TotalOfferingResponse.builder().totalOffering(total).result(result).build());
    }

    @Override
    public Mono<Void> deleteDataOfferingById(String offeringId) {
        dataOfferingCache.clear(offeringId);
        return dataOfferingRepository.findById(offeringId)
                .map(dataOfferingRepository::delete)
                .flatMap(e -> e);

    }

    @Override
    public String getOfferingTemplate() {
        RequestLicenseGrant licenseGrant = RequestLicenseGrant.builder().build();
        RequestIntendedUse intendedUse = RequestIntendedUse.builder().build();
        RequestContractParameters contractParameters = RequestContractParameters
                .builder()
                .hasLicenseGrant(licenseGrant)
                .hasIntendedUse(intendedUse)
                .build();
        RequestPaymentOnSubscription paymentOnSubscription = RequestPaymentOnSubscription.builder().build();
        RequestPaymentOnApi hasPaymentOnApi = RequestPaymentOnApi.builder().build();
        RequestPaymentOnUnit hasPaymentOnUnit = RequestPaymentOnUnit.builder().build();
        RequestPaymentOnSize hasPaymentOnSize = RequestPaymentOnSize.builder().build();
        RequestFreePrice hasFreePrice = RequestFreePrice.builder().build();
        RequestPricingModel pricingModel = RequestPricingModel.builder()
                .hasPaymentOnSubscription(paymentOnSubscription)
                .hasPaymentOnApi(hasPaymentOnApi)
                .hasPaymentOnUnit(hasPaymentOnUnit)
                .hasPaymentOnSize(hasPaymentOnSize)
                .hasFreePrice(hasFreePrice)
                .build();
        List<String> theme = List.of("required");
        List<RequestDistribution> distribution = List.of(RequestDistribution.builder().build());
        List<RequestDatasetInformation> datasetInformation = List.of(RequestDatasetInformation.builder().build());
        RequestDataset dataset = RequestDataset.builder()
                .theme(theme)
                .datasetInformation(datasetInformation)
                .distribution(distribution)
                .build();
        return new GsonBuilder().serializeNulls().setPrettyPrinting().create()
                .toJson(RequestDataOffering.builder()
                        .contractParameters(contractParameters)
                        .hasPricingModel(pricingModel)
                        .hasDataset(dataset)
                        .build());
    }

    private DataOffering offeringTransform(final DataOffering updateEntity, final DataOffering currentEntity) {

        return currentEntity.toBuilder()
                .owner(updateEntity.getOwner())
                .providerDid(updateEntity.getProviderDid())
                .active(updateEntity.isActive())
                .ownerConsentForm(updateEntity.getOwnerConsentForm())
                .inSharedNetwork(updateEntity.isInSharedNetwork())
                .personalData(updateEntity.isPersonalData())
                .dataOfferingTitle(updateEntity.getDataOfferingTitle())
                .dataOfferingDescription(updateEntity.getDataOfferingDescription())
                .category(updateEntity.getCategory())
                .status(updateEntity.getStatus())
                .dataOfferingExpirationTime(updateEntity.getDataOfferingExpirationTime())
                .contractParameters(updateEntity.getContractParameters())
                .hasPricingModel(updateEntity.getHasPricingModel())
                .hasDataset(updateEntity.getHasDataset())
                .build();

    }


    public Flux<DataOfferingDto> getByDataOfferingTitleAndPricingModelName(String dataOfferingTitle , String pricingModelName , final int page , final int size){
      return  dataOfferingRepository.findByDataOfferingTitleOrHasPricingModelPricingModelName(dataOfferingTitle , pricingModelName)
                .skip(page * size).take(size)
                .map(e-> mapper.entityToDto(e))
               .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry no offering found")));
    }

    public Flux<DataOfferingDto> getOfferingsByActiveAndCategory(final String category , final int page, final int size){
      return  dataOfferingRepository.findByCategoryAndActiveTrue(category.toLowerCase())
              .map(mapper::entityToDto)
              .sort(compareOfferingTime.reversed())
              .sort(compareOfferingTitle)
              .skip(page * size).take(size)
              .log(log.getName(), Level.FINE)
              .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for category: " + category)));
    }

    public Flux<DataOfferingDto> getOfferingsByActiveAndProvider(final String provider , final int page, final int size){
        return  dataOfferingRepository.findByProviderAndActiveTrue(provider.toLowerCase())
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for provider: " + provider)));
    }


    //--------------------Federated Search----------------------


   // @Async("asyncExecutor")

    public Flux<DataOfferingDto> gettingfromAnotherNodeByCategory(String category , ServerHttpRequest serverHttpRequest ,
                                                                  int page , int size)   {

//        String address = null;
//        String key = "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26";
//        System.out.println("Value of address  " + address);
//        try {
//            address = getURi(serverHttpRequest).get();
//            System.out.println("address value in try and catch block   " +address);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//         String location=null;
//        try {
//            location = getLocation(key, "http://95.211.3.250", category).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Getting Value of Location " + location);
//
//        System.out.println();
//        return webClient.build().get().uri(location+"8082/api/registration/offering/"+category.toLowerCase()).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .log(log.getName(), Level.FINE);

//        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);

        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));


        OkHttpClient client = new OkHttpClient();

        List<DataOfferingDto> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
//            System.out.println("Value of "+i + locations.get(i).substring(0,locations.get(i).length()-5));
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/offering-P/"+category).build();

            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                System.out.println(execute.toString());
                if(execute.code()!=404){
                    val =execute.body().string();
//                    System.out.println("Val : '\n"+ val);
                    obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                    obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                    obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    obj.registerModule( new JavaTimeModule());
                    final List<DataOfferingDto> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, DataOfferingDto.class));
                    System.out.println("After  List");
                    o.stream().forEach(e-> ArrList.add(e));
//                    o.stream().forEach(e-> System.out.println(e.toString()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        // WEBCLIENT
//        String Api =":8082/api/registration/offering-P/"+category;
//        final Flux<DataOfferingDto> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<DataOfferingDto> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//
//        final Flux<DataOfferingDto> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                .concatWith(offeringIdResFlux4).skip(page*size).take(size);
//        return offeringIdResFlux1;

        final List<String> nodes = nodesCache.getValue("nodes");
        nodes.stream().forEach(e-> System.out.println("from cache "+e));

        final List<DataOfferingDto> collect = ArrList.stream().skip(page * size).limit(size).collect(Collectors.toList());
        return Flux.fromIterable(collect);
    }


    //  -----------ADDING CATEGORY----------------------


    @Async("asyncExecutor")
    public Mono<Void> addDataCategory(String category , String key , String substring) {
//        System.out.println(serverHttpRequest.getURI());
//        final String substring = String.valueOf(serverHttpRequest.getURI()).substring(0, 19);
//        System.out.println("URI  "+substring);

        String address =substring+":8545";

        SeedsIndex seedsIndex = new SeedsIndex(address, key );

        try{

            seedsIndex.init();

            final String myNodeId = seedsIndex.getMyNodeId();

            final Optional<SearchEngineIndexRecord> indexRecordByNodeId = seedsIndex.getIndexRecordByNodeId(myNodeId);

            ArrayList<DataCategory> categoriesList = new ArrayList<>();
            final DataCategory[] dataCategories =
                    indexRecordByNodeId
                            .map(e -> e.getCategories()).get();

            Arrays.stream(dataCategories).forEach(e->{
                System.out.println(e.name());
                categoriesList.add(e);
            });

            if(categoriesList.size()>0 && categoriesList.size()<2) {
                System.out.println("Inside box 1");
                System.out.println(categoriesList.get(0));
                seedsIndex.setMyIndexRecord(new URI(address),
                        new DataCategory[]{

                                DataCategory.byLabel(String.valueOf(categoriesList.get(0))),
                                DataCategory.byLabel(category)

                        });
            }
            else if(categoriesList.size()>1 && categoriesList.size()<3) {
                System.out.println("Inside box 2");
                seedsIndex.setMyIndexRecord(new URI(address),
                        new DataCategory[]{
                                DataCategory.byLabel(String.valueOf(categoriesList.get(0))),
                                DataCategory.byLabel(String.valueOf(categoriesList.get(1))),
                                DataCategory.byLabel(category)

                        });
            }
            else if(categoriesList.size()>2 && categoriesList.size()<4) {
                System.out.println("Inside box 3");
                seedsIndex.setMyIndexRecord(new URI(address),
                        new DataCategory[]{
                                DataCategory.byLabel(String.valueOf(categoriesList.get(0))),
                                DataCategory.byLabel(String.valueOf(categoriesList.get(1))),
                                DataCategory.byLabel(String.valueOf(categoriesList.get(2))),
                                DataCategory.byLabel(category)
                        });
            }
            else if(categoriesList.size()==4) {
                System.out.println("Inside box 4");
                ArrayList<DataCategory> categoriesList2 = new ArrayList<>();
                categoriesList2.add(0,categoriesList.get(1));
                categoriesList2.add(1,categoriesList.get(2));
                categoriesList2.add(2,categoriesList.get(3));

                seedsIndex.setMyIndexRecord(new URI(address),
                        new DataCategory[]{

                                DataCategory.byLabel(String.valueOf(categoriesList2.get(0))),
                                DataCategory.byLabel(String.valueOf(categoriesList2.get(1))),
                                DataCategory.byLabel(String.valueOf(categoriesList2.get(2))),
                                DataCategory.byLabel(category)

                        });
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            seedsIndex.shutdown();
        }

        return null;
    }


    @Async("asyncExecutor")
    public void name(ServerHttpRequest serverHttpRequest ){

        final String substring1 = String.valueOf(serverHttpRequest.getURI()).substring(0, 19);

        String addr ="http://95.211.3.250";
        System.out.println("value of http request "+addr);
        // 0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26
        SeedsIndex seedsIndex = new SeedsIndex( addr+ ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");

        try {

            seedsIndex.init();

            System.out.println("Getting initiated");
            final Collection<SearchEngineIndexRecord> byDataCategory = seedsIndex.findByDataCategory(DataCategory.byLabel("economy"));
            final Iterator<SearchEngineIndexRecord> iterator = byDataCategory.iterator();

            System.out.println("Getting Location");
            final URI location1 = iterator.next().getLocation();
            final String substring = String.valueOf(location1).substring(0, 20);
            System.out.println(" value of SubString "+substring);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            seedsIndex.shutdown();
        }

    }


    public Flux<OfferingIdRes> gettingListOfOffering(  ServerHttpRequest  serverHttpRequest , int page , int size) throws ExecutionException, InterruptedException {
       String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250"+ ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }

        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);


        loca.stream().forEach(e-> System.out.println(e));


          locations.stream().map(e-> e.substring(0,19))
                  .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
       // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<OfferingIdRes> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/offerings-list-P").build();

            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                val =execute.body().string();
                System.out.println( val);
                obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final List<OfferingIdRes> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, OfferingIdRes.class));
                System.out.println("After  List");
                o.stream().forEach(e-> ArrList.add(e));
                o.stream().forEach(e-> System.out.println(e.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //WEBCLIENT
//        String Api =":8082/api/registration/offerings-list-P";
//        final Flux<OfferingIdRes> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<OfferingIdRes> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<OfferingIdRes> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<OfferingIdRes> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<OfferingIdRes> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                .concatWith(offeringIdResFlux4).skip(page*size).take(size);
//        return offeringIdResFlux1;
        final List<OfferingIdRes> collect = ArrList.stream().skip(page * size).limit(size).collect(Collectors.toList());
        return Flux.fromIterable(collect);
    }


    public Flux<ProviderIdResponse> gettingListOfProviders(  ServerHttpRequest  serverHttpRequest ) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250"+ ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        final List<String> locations = getLocations(seedsIndex);
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);


        loca.stream().forEach(e-> System.out.println(e));


        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<ProviderIdResponse> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/providers-lis").build();

            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                if(execute.code()!=404){
                    val =execute.body().string();
                    System.out.println( val);
                    obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                    obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                    obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                final List<ProviderIdResponse> o = obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, ProviderIdResponse.class));
//                System.out.println("After  List");
//                o.stream().forEach(e-> ArrList.add(e));
//                o.stream().forEach(e-> System.out.println(e.getProvider()));
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    final ProviderIdResponse[] providerIdResponses = gsonBuilder.create().fromJson(val, ProviderIdResponse[].class);
                  Arrays.stream(providerIdResponses).forEach(e-> ArrList.add(e));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //WEBCLIENT
//        String Api = ":8082/api/registration/providers-lis";
//        final Flux<ProviderIdResponse> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(ProviderIdResponse.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<ProviderIdResponse> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(ProviderIdResponse.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<ProviderIdResponse> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(ProviderIdResponse.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<ProviderIdResponse> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(ProviderIdResponse.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//
//        final Flux<ProviderIdResponse> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3).concatWith(offeringIdResFlux4);
//        return offeringIdResFlux1;

        return Flux.fromIterable(ArrList);

    }


    public Flux<OfferingIdRes> gettingFedListOfOfferingOnSharedNetwork(  ServerHttpRequest  serverHttpRequest , int page , int size) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);

        loca.stream().forEach(e-> System.out.println(e));

        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<OfferingIdRes> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/offerings-list/on-SharedNetwork-P").build();

            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                val =execute.body().string();
                System.out.println( val);
                obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final List<OfferingIdRes> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, OfferingIdRes.class));
                System.out.println("After  List");
                o.stream().forEach(e-> ArrList.add(e));
                o.stream().forEach(e-> System.out.println(e.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //WEBCLIENT
//        String Api = ":8082/api/registration/offerings-list/on-SharedNetwork-P";
//        final Flux<OfferingIdRes> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<OfferingIdRes> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<OfferingIdRes> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<OfferingIdRes> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//
//        final Flux<OfferingIdRes> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                .concatWith(offeringIdResFlux4).skip(page*size).take(size);
//        return offeringIdResFlux1;
        final List<OfferingIdRes> collect = ArrList.stream().skip(page * size).limit(size).collect(Collectors.toList());
        return Flux.fromIterable(collect);
    }


    public Flux<OfferingIdRes> gettingFedListOfOfferingOnActive(  ServerHttpRequest  serverHttpRequest, int page , int size) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);

        loca.stream().forEach(e-> System.out.println(e));

        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<OfferingIdRes> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        Flux<OfferingIdRes> log = null;

        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/offerings-list/on-active-P").build();

            ObjectMapper obj = new ObjectMapper();
            String val;

            try {
                final Response execute = client.newCall(request).execute();
                val =execute.body().string();
                System.out.println( val);
                obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final List<OfferingIdRes> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, OfferingIdRes.class));
                System.out.println("After  List");
                o.stream().forEach(e-> ArrList.add(e));
                o.stream().forEach(e-> System.out.println(e.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //WEBCLIENT
//        final Flux<OfferingIdRes> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244:8082/api/registration/offerings-list/on-active-P").retrieve()
//                .bodyToFlux(OfferingIdRes.class) .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<OfferingIdRes> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249:8082/api/registration/offerings-list/on-active-P").retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<OfferingIdRes> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list/on-active-P").retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<OfferingIdRes> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251:8082/api/registration/offerings-list/on-active-P").retrieve()
//                .bodyToFlux(OfferingIdRes.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<OfferingIdRes> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3).concatWith(offeringIdResFlux4).skip(page*size).take(size);
//
//        return offeringIdResFlux1;


        final List<OfferingIdRes> collect = ArrList.stream().skip(page * size).limit(size).collect(Collectors.toList());
        return Flux.fromIterable(collect);

    }

    private void getFlux(Flux<OfferingIdRes> log) throws InterruptedException {
        Thread.sleep(50);
        ArrayList<String> arrayList = new ArrayList<>();
       log.map(e-> {
           arrayList.add(e.getOffering());
           return e;
       });
       log.subscribe();
       arrayList.stream().forEach(e-> System.out.println());
    }


    public Flux<DataOfferingDto> gettingFedListOfOfferingTextSearch(  ServerHttpRequest  serverHttpRequest , String text , int page , int size) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);


        loca.stream().forEach(e-> System.out.println(e));


        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<DataOfferingDto> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/textSearch/text-P/"+text).build();

            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                if(execute.code()!=404){
                    val =execute.body().string();
//                    System.out.println("Val : '\n"+ val);
                    obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                    obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                    obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    obj.registerModule( new JavaTimeModule());
                    final List<DataOfferingDto> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, DataOfferingDto.class));
                    System.out.println("After  List");
                    o.stream().forEach(e-> ArrList.add(e));
                    o.stream().forEach(e-> System.out.println(e.toString()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //WEBCLIENT
//        String Api =":8082/api/registration/textSearch/text-P/"+text;
//        final Flux<DataOfferingDto> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<DataOfferingDto> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//
//        final Flux<DataOfferingDto> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3).concatWith(offeringIdResFlux4)
//                .skip(page*size).take(size);
//        return offeringIdResFlux1;

        final List<DataOfferingDto> collect = ArrList.stream().skip(page * size).limit(size).collect(Collectors.toList());
        return Flux.fromIterable(collect);
    }


    public Flux<DataOfferingDto> gettingFedListofActiveOfOfferingTextSearch(  ServerHttpRequest  serverHttpRequest , String text , int page , int size) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);


        loca.stream().forEach(e-> System.out.println(e));


        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<DataOfferingDto> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)
                    +":8082/api/registration/getActiveOfferingByText/"+text +"/text-P").build();

            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                if(execute.code()!=404){
                    val =execute.body().string();
//                    System.out.println("Val : '\n"+ val);
                    obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                    obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                    obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    obj.registerModule( new JavaTimeModule());
                    final List<DataOfferingDto> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, DataOfferingDto.class));
                    System.out.println("After  List");
                    o.stream().forEach(e-> ArrList.add(e));
                    o.stream().forEach(e-> System.out.println(e.toString()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //WEBCLIENT
//        String Api =":8082/api/registration/getActiveOfferingByText/"+text +"/text-P";
//        final Flux<DataOfferingDto> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<DataOfferingDto> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//
//        final Flux<DataOfferingDto> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                .concatWith(offeringIdResFlux4).skip(page*size).take(size);
//        return offeringIdResFlux1;

        return Flux.fromIterable(ArrList);
    }

    public List<DataOfferingDto> gettingFedListOfOfferingByProvider(  ServerHttpRequest  serverHttpRequest , String id) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);


        loca.stream().forEach(e-> System.out.println(e));


        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<DataOfferingDto> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/offering/"+id+"/providerId").build();

            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                if(execute.code()!=404) {
                    val =execute.body().string();
                    System.out.println( val);
                    obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                    obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                    obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    obj.registerModule( new JavaTimeModule());
                    final List<DataOfferingDto> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, DataOfferingDto.class));
                    System.out.println("After  List");
                    o.stream().forEach(e-> ArrList.add(e));
                    o.stream().forEach(e-> System.out.println(e.toString()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return ArrList;
    }

// by id
    public Mono<DataOfferingDto> gettingFederatedOffering(String offeringID , ServerHttpRequest  serverHttpRequest){
        String address = null;
        try {
            address = getURi(serverHttpRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        final DataOfferingDto value = dataOfferingCache.getValue(offeringID);
        if(value.getDataOfferingId()==null) {
            log.info("from if block");
//        final List<String> locations =  List.of("http://95.211.3.244", "http://95.211.3.249","http://95.211.3.250","http://95.211.3.251");
            List<String> locations = new ArrayList<>();
            if (nodesCache.getValue("nodes").size() == 0) {
                locations = getLocations(seedsIndex);
                nodesCache.adduserValue("nodes", locations);
            } else {
                locations = nodesCache.getValue("nodes");
            }
            locations.stream().forEach(e -> System.out.println(e));


            OkHttpClient client = new OkHttpClient();
            DataOfferingDto dataOfferingDto = null;
            log.info("Offering id : 63c91d9ef762242150a481e3");
            for (int i = 0; i < locations.size(); i++) {
                Request request = new Request.Builder()
                        .get()
                        .url(locations.get(i).substring(0, locations.get(i).length() - 5) + ":8082/api/registration/offering/" + offeringID + "/offeringId")
                        .build();
                ObjectMapper obj = new ObjectMapper();
                String val;
                try {
                    final Response execute = client.newCall(request).execute();

                    System.out.println(i + "  Value of code " + execute.code());
                    if (execute.code() != 404) {
                        val = execute.body().string();
                        obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                        obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                        obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
                        obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        obj.registerModule(new JavaTimeModule());
                        dataOfferingDto = obj.readValue(val, DataOfferingDto.class);
                        dataOfferingCache.adduserValue(offeringID, dataOfferingDto);
                        break;
                    } else if (i == locations.size() - 1 && execute.code() == 404) {
                        throw new NotFoundException(HttpStatus.NOT_FOUND, "Sorry Offering Id not found");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            //Webclient

//        final DataOfferingDto value = dataOfferingCache.getValue(offeringID);
//        if(value.getDataOfferingId()==null) {
//
//            log.info("not found in cache");
//            String Api = ":8082/api/registration/offering/" + offeringID + "/offeringId";
//            final Mono<DataOfferingDto> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//            final Mono<DataOfferingDto> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//            final Mono<DataOfferingDto> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//
//            final Mono<DataOfferingDto> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//
//            final Flux<DataOfferingDto> dataOfferingDtoFlux = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                    .concatWith(offeringIdResFlux4);
//
//            final Mono<DataOfferingDto> from = Mono.from(dataOfferingDtoFlux.take(1));
//            from.subscribe(e -> dataOfferingCache.adduserValue(offeringID, e));
//
//            return from;
//
//
//        }
//        else{
//            log.info("found in cache");
//            return Mono.just(value);
//        }


            return Mono.just(dataOfferingDto);
        }
        else{
            log.info("from else block");
            return Mono.just(value);
        }
    }

    public Mono<ContractParametersResponse> gettingFederatedContractParameter(String offeringID , ServerHttpRequest  serverHttpRequest){
        String address = null;
        try {
            address = getURi(serverHttpRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
//        final DataOfferingDto value = dataOfferingCache.getValue(offeringID);
//        if(value.getDataOfferingId()==null) {
            log.info("from if block");
//        final List<String> locations =  List.of("http://95.211.3.244", "http://95.211.3.249","http://95.211.3.250","http://95.211.3.251");
            List<String> locations = new ArrayList<>();
            if (nodesCache.getValue("nodes").size() == 0) {
                locations = getLocations(seedsIndex);
                nodesCache.adduserValue("nodes", locations);
            } else {
                locations = nodesCache.getValue("nodes");
            }
            locations.stream().forEach(e -> System.out.println(e));


            OkHttpClient client = new OkHttpClient();
            ContractParametersResponse dataOfferingDto = null ;
            log.info("Offering id : 63c91d9ef762242150a481e3");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println(locations.get(i).substring(0, locations.get(i).length() - 5) );
                Request request = new Request.Builder()
                        .get()
                        .url(locations.get(i).substring(0, locations.get(i).length() - 5) + ":8082/api/registration/contract-parameter/" + offeringID + "/offeringId")
                        .build();
                ObjectMapper obj = new ObjectMapper();
                GsonBuilder gsonBuilder = new GsonBuilder();
                String val;
                try {
                    final Response execute = client.newCall(request).execute();

                    if (execute.code() != 404) {
                        val = execute.body().string();

                        obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                        obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                        obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
                        obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        obj.registerModule(new JavaTimeModule());

                        final ContractParametersResponse contractParametersResponse = gsonBuilder.create().fromJson(val, ContractParametersResponse.class);

                        dataOfferingDto = contractParametersResponse;

                        break;
                    } else if (i == locations.size() - 1 && execute.code() == 404) {
                        throw new NotFoundException(HttpStatus.NOT_FOUND, "Sorry Offering Id not found");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            // Webclient

//        final DataOfferingDto value = dataOfferingCache.getValue(offeringID);
//        if(value.getDataOfferingId()==null) {
//
//            log.info("not found in cache");
//            String Api = ":8082/api/registration/offering/" + offeringID + "/offeringId";
//            final Mono<DataOfferingDto> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//            final Mono<DataOfferingDto> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//            final Mono<DataOfferingDto> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//
//            final Mono<DataOfferingDto> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251" + Api).retrieve()
//                    .bodyToMono(DataOfferingDto.class)
//                    .onErrorResume(WebClientResponseException.class,
//                            ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex));
//
//            final Flux<DataOfferingDto> dataOfferingDtoFlux = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                    .concatWith(offeringIdResFlux4);
//
//            final Mono<DataOfferingDto> from = Mono.from(dataOfferingDtoFlux.take(1));
//            from.subscribe(e -> dataOfferingCache.adduserValue(offeringID, e));
//
//            return from;
//
//
//        }
//        else{
//            log.info("found in cache");
//            return Mono.just(value);
//        }


            return Mono.just(dataOfferingDto);
//        }
//        else{
//            return Mono.just(value);
//        }
    }


    public DataOfferingDto getOfferingCache(String id){

        dataOfferingCache.clear(id);

        return  null;

    }


    public List<CategoriesList> getRegisteredCategories(ServerHttpRequest serverHttpRequest , String key ){

        String address = null;
        try {
            address = getURi(serverHttpRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //  String address = "http://95.211.3.251:8545";

        SeedsIndex seedsIndex = new SeedsIndex(address+":8545", key);

        try {
            seedsIndex.init();
            final Optional<SearchEngineIndexRecord> indexRecordByNodeId = seedsIndex.getIndexRecordByNodeId(seedsIndex.getMyNodeId());
           return Arrays.stream(indexRecordByNodeId.get()
                            .getCategories())
                            .map(e -> CategoriesList.builder()
                            .name(e.name())
                            .description(e.description())
                            .build()).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Async("asyncExecutor")
    private Future<String> getLocation(String key , String address , String category ) {

        SeedsIndex seedsIndex = new SeedsIndex(address + ":8545",
                key);
        // 0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26

        try {
            seedsIndex.init();

            System.out.println("Getting initiated");
            final Collection<SearchEngineIndexRecord> byDataCategory = seedsIndex.findByDataCategory(DataCategory.byLabel(category));
            final Iterator<SearchEngineIndexRecord> iterator = byDataCategory.iterator();

            System.out.println("Getting Location");
            final URI location1 = iterator.next().getLocation();
            final AsyncResult<String> stringAsyncResult = new AsyncResult<>(String.valueOf(location1).substring(0, 20));

            return stringAsyncResult;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
       finally {
            seedsIndex.shutdown();
        }
        return null;
    }


    public  List<String>getLocations(SeedsIndex seedsIndex){
        List<String>  location= new ArrayList<>();
        try {

            seedsIndex.init();
            final Collection<SearchEngineIndexRecord> byDataCategory = seedsIndex.findByDataCategory(null);
            for(var se : byDataCategory){
                location.add(String.valueOf(se.getLocation())) ;
            }
            return location;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            seedsIndex.shutdown();
        }
        return null;
    }

    @Async("asyncExecutor")
    public Future<String> getURi(ServerHttpRequest  serverHttpRequest){
        if(serverHttpRequest!=null){
            System.out.println("Getting URI "+serverHttpRequest.getURI());
            System.out.println("Getting remote hostString "+serverHttpRequest.getRemoteAddress().getHostString());
            System.out.println("Getting path with application  "+serverHttpRequest.getPath().pathWithinApplication().value());
            System.out.println("Getting remote hostName "+serverHttpRequest.getRemoteAddress().getAddress().getHostName());
            System.out.println("Getting local hostString "+serverHttpRequest.getLocalAddress().getHostString());
            return   new AsyncResult<>(String.valueOf(serverHttpRequest.getURI()).substring(0, 19)) ;
        }
        return null;

    }

    public Flux<DataOfferingDto> getByActiveAndShareDataWithThirdParty(boolean active , boolean shareDataWithThirdParty){
        return dataOfferingRepository.findByActiveAndContractParametersHasIntendedUseShareDataWithThirdParty(active , shareDataWithThirdParty)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry no Offering found")))
                .map(e-> mapper.entityToDto(e));
    }
    public Flux<DataOfferingDto> getBySharedNetAndTransferableAndFreePrice(boolean shared , boolean transfer, boolean freePrice){
        return dataOfferingRepository.
                findByContractParametersHasIntendedUseShareDataWithThirdPartyAndContractParametersHasLicenseGrantTransferableAndHasPricingModelHasFreePriceHasPriceFree(shared,transfer,freePrice)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry no Offering found")))
                .map(e-> mapper.entityToDto(e));
    }

    public void deleteAll(){
        System.out.println("delete being called");
       deleteAllOffering();
    }

    public Flux<DataOfferingDto> gettingFedListOfActiveOfferingByCategory(  ServerHttpRequest  serverHttpRequest , String category,int page,int size) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);


        loca.stream().forEach(e-> System.out.println(e));


        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<DataOfferingDto> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/ActiveOfferingByCategory-P/"+category).build();


            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                if(execute.code()!=404){
                    val =execute.body().string();
//                    System.out.println("Val : '\n"+ val);
                    obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                    obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                    obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    obj.registerModule( new JavaTimeModule());
                    final List<DataOfferingDto> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, DataOfferingDto.class));
                    System.out.println("After  List");
                    o.stream().forEach(e-> ArrList.add(e));
                    o.stream().forEach(e-> System.out.println(e.toString()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //WEBCLIENT
//        String Api =":8082/api/registration/ActiveOfferingByCategory-P/"+category;
//        final Flux<DataOfferingDto> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<DataOfferingDto> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//
//        final Flux<DataOfferingDto> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                .concatWith(offeringIdResFlux4).skip(page*size).take(size);
//        return offeringIdResFlux1;


        final List<DataOfferingDto> collect = ArrList.stream().skip(page * size).limit(size).collect(Collectors.toList());
        return Flux.fromIterable(collect);
    }

    public Flux<DataOfferingDto> gettingFedListOfActiveOfferingByProvider(ServerHttpRequest  serverHttpRequest , String provider , int page ,int size) throws ExecutionException, InterruptedException {
        String address =getURi(serverHttpRequest).get();

        //  address = getURi(serverHttpRequest).get();
        SeedsIndex seedsIndex = new SeedsIndex("http://95.211.3.250" + ":8545",
                "0x91ca5769686d3c0ba102f0999140c1946043ecdc1c3b33ee3fd2c80030e46c26");
        List<String> locations = new ArrayList<>();
        if(nodesCache.getValue("nodes").size()==0){
            locations = getLocations(seedsIndex);
            nodesCache.adduserValue("nodes",locations);
        }
        else{
            locations = nodesCache.getValue("nodes");
        }
        locations.stream().forEach(e-> System.out.println(e));
        locations.stream().forEach(e-> System.out.println(e));
        Set<String> loca = new HashSet<>(locations);


        loca.stream().forEach(e-> System.out.println(e));


        locations.stream().map(e-> e.substring(0,19))
                .forEach(e-> System.out.println(e));
        System.out.println(locations.get(0).substring(0, 19));
        // final Flux<OfferingIdResponse> offeringIdResponseFlux = webClient.build().get().uri("http://95.211.3.250:8082/api/registration/offerings-list").retrieve().bodyToFlux(OfferingIdResponse.class);

        OkHttpClient client = new OkHttpClient();

        List<DataOfferingDto> ArrList = new ArrayList<>();

        System.out.println("Value of size "+ locations.size());
        for(int i=0; i<locations.size();i++){
            System.out.println("Value of i ="+i);
            Request request = new Request.Builder().get().url(locations.get(i).substring(0,locations.get(i).length()-5)+":8082/api/registration/ActiveOfferingByProvider/"+provider+"/providerId-P").build();


            ObjectMapper obj = new ObjectMapper();
            String val;
            try {
                final Response execute = client.newCall(request).execute();
                if(execute.code()!=404){
                    val =execute.body().string();
//                    System.out.println("Val : '\n"+ val);
                    obj.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
                    obj.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
                    obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    obj.registerModule( new JavaTimeModule());
                    final List<DataOfferingDto> o= obj.readValue(val, obj.getTypeFactory().constructParametricType(List.class, DataOfferingDto.class));
                    System.out.println("After  List");
                    o.stream().forEach(e-> ArrList.add(e));
                    o.stream().forEach(e-> System.out.println(e.toString()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

//        String Api = ":8082/api/registration/ActiveOfferingByProvider/"+provider+"/providerId-P";
//        final Flux<DataOfferingDto> offeringIdResFlux = webClient.build().get().uri("http://95.211.3.244"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux2 = webClient.build().get().uri("http://95.211.3.249"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//        final Flux<DataOfferingDto> offeringIdResFlux3 = webClient.build().get().uri("http://95.211.3.250"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//        final Flux<DataOfferingDto> offeringIdResFlux4 = webClient.build().get().uri("http://95.211.3.251"+Api).retrieve()
//                .bodyToFlux(DataOfferingDto.class)
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getRawStatusCode() == 404 ? Flux.empty() : Mono.error(ex));
//
//
//        final Flux<DataOfferingDto> offeringIdResFlux1 = offeringIdResFlux.concatWith(offeringIdResFlux2).concatWith(offeringIdResFlux3)
//                .concatWith(offeringIdResFlux4).skip(page*size).take(size);
//        return offeringIdResFlux1;

        final List<DataOfferingDto> collect = ArrList.stream().skip(page * size).limit(size).collect(Collectors.toList());
        return Flux.fromIterable(collect);
    }

        //----------------------------------------------------------

    final Comparator<CategoriesList> comparingCategoriesList = Comparator.comparing(e-> e.getName());
    final Comparator<DataOfferingDto> compareOfferingTime = Comparator.comparing(DataOfferingDto::getCreatedAt);
    final Comparator<DataOfferingDto> compareOfferingTitle = Comparator.comparing(DataOfferingDto::getDataOfferingTitle);
    final Comparator<DataOfferingDto> compareByProvider = Comparator.comparing(DataOfferingDto::getProvider);

}
