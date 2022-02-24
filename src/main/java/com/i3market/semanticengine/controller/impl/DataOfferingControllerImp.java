package com.i3market.semanticengine.controller.impl;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
import com.i3market.semanticengine.controller.DataOfferingController;
import com.i3market.semanticengine.service.DataOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class DataOfferingControllerImp implements DataOfferingController {

    private final DataOfferingService dataOfferingService;

    @Override
    public ResponseEntity<Flux<CategoriesList>> getCategoryList() {
        return ResponseEntity.ok(dataOfferingService.getCategoryList());
    }

    @Override
    public ResponseEntity<Mono<DataOfferingDto>> createDataOffering(final RequestNewDataOffering dto) {
        return ResponseEntity.ok(dataOfferingService.createDataOffering(dto));
    }

    @Override
    public ResponseEntity<Mono<DataOfferingDto>> updateDataOffering(final DataOfferingDto request) {
        return ResponseEntity.ok(dataOfferingService.updateDataOffering(request));
    }

    @Override
    public ResponseEntity<Mono<DataOfferingDto>> getDataOfferingById(final String id) {
        return ResponseEntity.ok(dataOfferingService.getDataOfferingById(id));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteAllOffering() {
        return ResponseEntity.ok(dataOfferingService.deleteAllOffering());
    }

    @Override
    public ResponseEntity<Flux<OfferingIdResponse>> getOfferingList(final int page, final int size) {
        return ResponseEntity.ok(dataOfferingService.getOfferingList(page, size));
    }

    @Override
    public ResponseEntity<Flux<DataOfferingDto>> getOfferingByProvider(final String provider, final int page, final int size) {
        return ResponseEntity.ok(dataOfferingService.getOfferingByProvider(provider, page, size));
    }

    @Override
    public ResponseEntity<Flux<DataOfferingDto>> getOfferingByCategory(final String category, final int page, final int size) {
        return ResponseEntity.ok(dataOfferingService.getOfferingByCategory(category, page, size));
    }

    @Override
    public ResponseEntity<Mono<ContractParametersResponse>> getContractParameterByOfferingId(final String offeringId) {
        return ResponseEntity.ok(dataOfferingService.getContractParameterByOfferingId(offeringId));
    }

    @Override
    public ResponseEntity<Flux<ProviderIdResponse>> getProviderListByCategory(final String category, final int page, final int size) {
        return ResponseEntity.ok(dataOfferingService.getProviderListByCategory(category, page, size));
    }

    @Override
    public ResponseEntity<Mono<TotalOfferingResponse>> getOfferingByProviderIdAndCategorySorted(final String providerId, final String category,
                                                                                                final int page, final int size,
                                                                                                final String orderBy, final String sortBy) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(dataOfferingService.getOfferingByProviderIdAndCategorySorted(providerId, category, page, size, orderBy, sortBy));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteDataOfferingById(final String offeringId) {
        return ResponseEntity.ok(dataOfferingService.deleteDataOfferingById(offeringId));
    }


}
