package com.i3market.semanticengine.controller.impl;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
import com.i3market.semanticengine.controller.DataOfferingController;
import com.i3market.semanticengine.service.DataOfferingService;
import com.i3market.semanticengine.service.impl.TextSearchClass;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequiredArgsConstructor
public class DataOfferingControllerImp implements DataOfferingController {

    private final DataOfferingService dataOfferingService;

    @Autowired
    private TextSearchClass textSearchClass;

    @Autowired
    private TextSearchClass searchClass;

    @Override
    public ResponseEntity<Flux<CategoriesList>> getCategoryList(ServerHttpRequest serverHttpRequest) {
        return ResponseEntity.ok(dataOfferingService.getCategoryList(serverHttpRequest));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Mono<DataOfferingDto>> createDataOffering(final RequestDataOffering dto , ServerHttpRequest serverHttpRequest) {
        log.info("POST a new offering");
        return ResponseEntity.ok(dataOfferingService.createDataOffering(dto, serverHttpRequest));
    }

    @Override
    public ResponseEntity<Mono<DataOfferingDto>> updateDataOffering(final DataOfferingDto request) {
        log.info("UPDATE an existing offering");
        return ResponseEntity.ok(dataOfferingService.updateDataOffering(request));
    }

    @Override
    public ResponseEntity<Mono<DataOfferingDto>> getDataOfferingById(final String id) {
        log.info("GET an offering by offeringI");
        return ResponseEntity.ok(dataOfferingService.getDataOfferingById(id));
    }

    @Override
    public ResponseEntity<Flux<OfferingIdResponse>> getOfferingList(final int page, final int size) {
        log.info("GET an offering list");
        return ResponseEntity.ok(dataOfferingService.getOfferingList(page, size));
    }

    @Override
    public ResponseEntity<Flux<DataOfferingDto>> getOfferingByProvider(final String provider, final int page, final int size) {
        log.info("GET a list of offering by provider");
        return ResponseEntity.ok(dataOfferingService.getOfferingByProvider(provider, page, size));
    }

    @Override
    public ResponseEntity<Flux<DataOfferingDto>> getOfferingByCategory(final String category, final int page, final int size) {
        log.info("GET a list of offering by category");
        return ResponseEntity.ok(dataOfferingService.getOfferingByCategory(category, page, size));
    }

    @Override
    public ResponseEntity<Flux<DataOfferingDto>> getByTitleAndPriceModelName(String dataOfferingTitle, String pricingModelName, int page, int size) {

            return ResponseEntity.ok(dataOfferingService
                    .getByDataOfferingTitleAndPricingModelName(dataOfferingTitle, pricingModelName, page,size));
    }

    @Override
    public ResponseEntity<Mono<ContractParametersResponse>> getContractParameterByOfferingId(final String offeringId) {
        log.info("GET contract parameter by offering Id");
        return ResponseEntity.ok(dataOfferingService.getContractParameterByOfferingId(offeringId));
    }

    @Override
    public ResponseEntity<Flux<ProviderIdResponse>> getProviderListByCategory(final String category, final int page, final int size) {
        log.info("GET all provider by category");
        return ResponseEntity.ok(dataOfferingService.getProviderListByCategory(category, page, size));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Mono<TotalOfferingResponse>> getOfferingByProviderIdAndCategorySorted(final String providerId, final String category,
                                                                                                final int page, final int size,
                                                                                                final String sortBy, final String orderIn) {
        log.info("GET total of offering and offering list");
        return ResponseEntity.ok(dataOfferingService.getOfferingByProviderIdAndCategorySorted(providerId, category, page, size, sortBy, orderIn));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteDataOfferingById(final String offeringId) {
        log.info("DELETE offering by offeringId");
        return ResponseEntity.ok(dataOfferingService.deleteDataOfferingById(offeringId));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<String> getOfferingTemplate() {
        log.info("GET offering template in JSON format");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "DataOfferingTemplate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dataOfferingService.getOfferingTemplate());
    }

    @Override
    public Flux<DataOfferingDto> getTextSearchMono(String text, int page, int size) {
        return textSearchClass.getTextSearchReact(text,page,size);
    }

    @Override
    public ResponseEntity<Flux<OfferingIdResponse>> getOfferingListonActive(final int page, final int size) {
        log.info("GET an offering list");
        return ResponseEntity.ok(dataOfferingService.getOfferingListonActive(page, size));
    }

    public ResponseEntity<Flux<OfferingIdResponse>> getOfferingListonSharedNetwork(final int page, final int size) {
        log.info("GET an offering list");
        return ResponseEntity.ok(dataOfferingService.getOfferingListonSharedNetwork(page, size));
    }

    @GetMapping("/getOfferingByActiveAndShareDataWithThirdParty/{active}/{shareDataWithThirdParty}")
    @Operation(summary = "get Offering on boolean values on active and shared data with third party")
    public Flux<DataOfferingDto> getByActiveOrShareDataWithThirdParty(@PathVariable(name = "active") boolean active
            ,@PathVariable(name = "shareDataWithThirdParty") boolean shareDataWithThirdParty){
        return dataOfferingService.getByActiveAndShareDataWithThirdParty(active, shareDataWithThirdParty);
    }
    @GetMapping("/getOfferingBySharedAndTransferableAndFreePrice/{shared}/{transfer}/{freePrice}")
    @Operation(summary = "get Offering on boolean values on sharedNetworkWithThirdParty and transferable and freePrice ")
    public Flux<DataOfferingDto> getBySharedNetAndTransferableAndFreePrice(@PathVariable(name = "shared") boolean shared
            ,@PathVariable(name = "transfer") boolean transfer  ,@PathVariable(name = "freePrice") boolean freePrice){
        return dataOfferingService.getBySharedNetAndTransferableAndFreePrice(shared,transfer,freePrice);
    }
    @DeleteMapping("/deletAllOfferings")
    public void deleteAllOfferings(){
        dataOfferingService.deleteAll();
    }

}
