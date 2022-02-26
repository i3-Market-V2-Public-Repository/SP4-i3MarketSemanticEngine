package com.i3market.semanticengine.service.impl;

import com.google.gson.GsonBuilder;
import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.entity.DataOffering;
import com.i3market.semanticengine.common.domain.request.*;
import com.i3market.semanticengine.common.domain.response.*;
import com.i3market.semanticengine.exception.InvalidInputException;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.mapper.Mapper;
import com.i3market.semanticengine.repository.DataOfferingRepository;
import com.i3market.semanticengine.repository.DataProviderRepository;
import com.i3market.semanticengine.service.DataOfferingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static java.util.Objects.isNull;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataOfferingServiceImpl implements DataOfferingService {

    @Value("${category-list.url}")
    private String categoryListUrl;

    private final DataOfferingRepository dataOfferingRepository;

    private final DataProviderRepository dataProviderRepository;

    private final Mapper mapper;

    private final WebClient.Builder webClient;

    private final SimpleDateFormat simpleFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");

    @Override
    public Flux<CategoriesList> getCategoryList() {
        return webClient.build().get().uri(categoryListUrl).retrieve()
                .bodyToFlux(CategoriesList.class)
                .log(log.getName(), Level.FINE);
    }

    @Override
    @SneakyThrows
    public Mono<DataOfferingDto> createDataOffering(final RequestNewDataOffering dto) {

        final Mono<DataProviderDto> dataProvider = dataProviderRepository.findByProviderId(dto.getProvider())
                .map(mapper::entityToDto);
        if (isNull(dataProvider.toFuture().get())) {
            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "We are sorry! The provider " + dto.getProvider() + " has not been registered");
        }

        final var entity = mapper.requestToEntity(dto);
        entity.setCreatedAt(simpleFormat.format(new Date()));
        final var newEntity = dataOfferingRepository.save(entity);
        return newEntity.log(log.getName(), Level.FINE)
                .onErrorMap(RuntimeException.class,
                        err -> new InvalidInputException(HttpStatus.BAD_REQUEST, "Error"))
                .map(mapper::entityToDto);
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
                .map(e -> OfferingIdResponse.builder().offering(e.getOfferingId()).build());
    }

    @Override
    public Mono<DataOfferingDto> updateDataOffering(final DataOfferingDto body) {
        final var currentEntity = dataOfferingRepository.findById(body.getOfferingId());
        final var updateEntity = mapper.dtoToEntity(body);
        return currentEntity.log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! Data Offering id" + body.getOfferingId() + " does not exist")))
                .map(e -> offeringTransform(updateEntity, e).toBuilder().updatedAt(simpleFormat.format(new Date())).build())
                .flatMap(dataOfferingRepository::save)
                .map(mapper::entityToDto);
    }

    @Override
    public Flux<DataOfferingDto> getOfferingByProvider(final String provider, final int page, final int size) {
        return dataOfferingRepository.findByProvider(provider)
                .map(mapper::entityToDto)
                .sort(compareOfferingTime.reversed())
                .sort(compareOfferingTitle)
                .skip(page * size).take(size)
                .log(log.getName(), Level.FINE)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Were are sorry! No data offering found for provider: " + provider)));
    }

    @Override
    public Flux<DataOfferingDto> getOfferingByCategory(final String category, final int page, final int size) {
        return dataOfferingRepository.findByCategory(category)
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
        final String category = inputCategory.strip();
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
                .toJson(RequestNewDataOffering.builder()
                        .contractParameters(contractParameters)
                        .hasPricingModel(pricingModel)
                        .hasDataset(dataset)
                        .build());
    }

    private DataOffering offeringTransform(final DataOffering updateEntity, final DataOffering currentEntity) {
        return currentEntity.toBuilder()
                .owner(updateEntity.getOwner())
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


    final Comparator<DataOfferingDto> compareOfferingTime = Comparator.comparing(DataOfferingDto::getCreatedAt);
    final Comparator<DataOfferingDto> compareOfferingTitle = Comparator.comparing(DataOfferingDto::getDataOfferingTitle);
    final Comparator<DataOfferingDto> compareByProvider = Comparator.comparing(DataOfferingDto::getProvider);
}
