package com.i3market.semanticengine.controller.impl;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
import com.i3market.semanticengine.controller.DataOfferingController;
import com.i3market.semanticengine.service.DataOfferingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequiredArgsConstructor
public class DataOfferingControllerImp implements DataOfferingController {

    private final DataOfferingService dataOfferingService;

    @Override
    public ResponseEntity<Flux<CategoriesList>> getCategoryList() {
        return ResponseEntity.ok(dataOfferingService.getCategoryList());
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Mono<DataOfferingDto>> createDataOffering(final RequestNewDataOffering dto) {
        log.info("POST a new offering");
        return ResponseEntity.ok(dataOfferingService.createDataOffering(dto));
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
    public ResponseEntity<Mono<Void>> deleteAllOffering() {
        log.info("DELETE all offerings");
        return ResponseEntity.ok(dataOfferingService.deleteAllOffering());
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
}
