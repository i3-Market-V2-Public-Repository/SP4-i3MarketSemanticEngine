package com.i3market.semanticengine.controller.impl;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import com.i3market.semanticengine.controller.DataOfferingController;
import com.i3market.semanticengine.service.DataOfferingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public ResponseEntity<Mono<DataOfferingDto>> createDataOffering(final RequestNewDataOffering dto) {
        return ResponseEntity.ok(dataOfferingService.createDataOffering(dto));
    }

    @Override
    public ResponseEntity<Mono<DataOfferingDto>> getDataOfferingById(final String id) {
        return ResponseEntity.ok(dataOfferingService.getDataOfferingById(id));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteAllOffering() {
        return ResponseEntity.ok(dataOfferingService.deleteAllOffering());
    }

}
