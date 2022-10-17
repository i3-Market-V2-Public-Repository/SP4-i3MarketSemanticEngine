package com.i3market.semanticengine.controller.impl;


import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.entity.OfferingIdRes;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import com.i3market.semanticengine.exception.NotFoundException;
import com.i3market.semanticengine.service.impl.DataOfferingServiceImpl;
import com.i3market.semanticengine.service.impl.TextSearchClass;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.i3market.semanticengine.common.constant.Constants.SEMANTIC_ENGINE_MAIN_PATH;

@RestController
@RequestMapping(SEMANTIC_ENGINE_MAIN_PATH)
@Tag(name = "Federated Search")
public class FederatedController {

    @Autowired
    private TextSearchClass textSearchClass;

    @Autowired
    private DataOfferingServiceImpl dataOfferingService;


    @GetMapping("/federated-offering/{category}")
    public Flux<DataOfferingDto> getOfferingByCategory(@PathVariable(name = "category") String category , ServerHttpRequest serverHttpRequest ){

       return dataOfferingService.gettingfromAnotherNodeByCategory(category , serverHttpRequest);

        //  return ResponseEntity.ok(offering.get());


    }

    @PostMapping("/federated-category/{category}/{key}")
    public ResponseEntity<Mono<Void>> postingCategory(@PathVariable(name = "category") String category
            , @PathVariable(name = "key") String key , ServerHttpRequest serverHttpRequest){
        try {
            dataOfferingService.addDataCategory(category, key , serverHttpRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("/getName")
    public String name(ServerHttpRequest serverHttpRequest ) throws ExecutionException, InterruptedException {

      dataOfferingService.name(serverHttpRequest);

        return "allDone";
    }
   // @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/federated-offerings-list")
    public ResponseEntity<List<OfferingIdRes>> getOfferingList( ServerHttpRequest  serverHttpRequest){
        try {
            return ResponseEntity.ok(dataOfferingService.gettingListOfOffering(serverHttpRequest));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/federated-providers-list")
    public ResponseEntity<List<ProviderIdResponse>> getProviderList(ServerHttpRequest  serverHttpRequest){
        try {
            return ResponseEntity.ok(dataOfferingService.gettingListOfProviders(serverHttpRequest));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/federated-offerings-list/on-SharedNetwork")
    @Operation(summary = "getting offering List on shared network in federated search")
    public ResponseEntity<List<OfferingIdRes>> getListOnSharedNetwork( ServerHttpRequest  serverHttpRequest){
        try {
            return ResponseEntity.ok(dataOfferingService.gettingFedListOfOfferingOnSharedNetwork(serverHttpRequest));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/federated-offerings-list/on-Active")
    @Operation(summary = "getting offering List on active in federated search")
    public ResponseEntity<List<OfferingIdRes>> getListOnActive( ServerHttpRequest  serverHttpRequest){
        try {
            return ResponseEntity.ok(dataOfferingService.gettingFedListOfOfferingOnActive(serverHttpRequest));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/federated-offering/{id}/offeringId")
    public DataOfferingDto getOffering(@PathVariable(name = "id") String id ,  ServerHttpRequest  serverHttpRequest){
      return  dataOfferingService.gettingFederatedOffering(id , serverHttpRequest);
    }


    @GetMapping("/federated-offering/{id}/providerId")
    public List<DataOfferingDto> getOfferingbyProvider(@PathVariable(name = "id") String id , ServerHttpRequest  serverHttpRequest){
        try {
            return  dataOfferingService.gettingFedListOfOfferingByProvider(serverHttpRequest,id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }
    @GetMapping("/getRegisteredCategories/{key}")
    public Flux<CategoriesList> getRegisteredCategories( ServerHttpRequest serverHttpRequest , @PathVariable(name = "key") String key){
      return Flux.fromIterable(dataOfferingService.getRegisteredCategories(serverHttpRequest , key))
              .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "Sorry! no registered categories found")));
    }

    @GetMapping("/federated-offering/getActiveOfferingByText/{text}/text")
    public List<DataOfferingDto> getOfferingTextSearch(@PathVariable(name = "text") String text ,  ServerHttpRequest  serverHttpRequest)  {
        try {
            return  dataOfferingService.gettingFedListofActiveOfOfferingTextSearch(serverHttpRequest,text);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }
    @GetMapping("/federated-offering/textSearch/text/{text}")
    public List<DataOfferingDto> getActiveOfferingTextSearch(@PathVariable(name = "text") String text ,  ServerHttpRequest  serverHttpRequest)  {
        try {
            return  dataOfferingService.gettingFedListOfOfferingTextSearch(serverHttpRequest,text);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @GetMapping("/federated-activeOffering/{category}")
    public Flux<DataOfferingDto> getActiveOfferingByCategory(@PathVariable(name = "category") String category , ServerHttpRequest serverHttpRequest )  {

        try {
            return dataOfferingService.gettingFedListOfActiveOfferingByCategory(  serverHttpRequest, category);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       return null;

    }
    @GetMapping("/federated-activeOffering/{id}/providerId")
    public Flux<DataOfferingDto> getActiveOfferingByProvider(@PathVariable(name = "id") String id , ServerHttpRequest  serverHttpRequest){
        try {
            return  dataOfferingService.gettingFedListOfActiveOfferingByProvider(serverHttpRequest,id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }


}
