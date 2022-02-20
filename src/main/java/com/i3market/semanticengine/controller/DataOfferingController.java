package com.i3market.semanticengine.controller;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.i3market.semanticengine.common.constant.Constants.SEMANTIC_ENGINE_MAIN_PATH;

@RequestMapping(SEMANTIC_ENGINE_MAIN_PATH)
@Tag(name = "Data Offering API")
public interface DataOfferingController {

    @GetMapping("/categories-list")
    ResponseEntity<Flux<CategoriesList>> getCategoryList();

    @PostMapping("/data-offering")
    ResponseEntity<Mono<DataOfferingDto>> createDataOffering(@Valid @NotNull @RequestBody final RequestNewDataOffering request);

    @GetMapping("/offering/{id}/offeringId")
    ResponseEntity<Mono<DataOfferingDto>> getDataOfferingById(@PathVariable(name = "id") final String id);

    @DeleteMapping("/data-offering/delete/all")
    ResponseEntity<Mono<Void>> deleteAllOffering();

}
