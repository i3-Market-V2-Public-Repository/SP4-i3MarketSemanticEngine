package com.i3market.semanticengine.controller;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ExecutionException;

import static com.i3market.semanticengine.common.constant.Constants.SEMANTIC_ENGINE_MAIN_PATH;

@RequestMapping(SEMANTIC_ENGINE_MAIN_PATH)
@Tag(name = "Data Offering API")
public interface DataOfferingController {

    @GetMapping("/categories-list")
    ResponseEntity<Flux<CategoriesList>> getCategoryList();

    @PostMapping("/data-offering")
    ResponseEntity<Mono<DataOfferingDto>> createDataOffering(@Valid @NotNull @RequestBody final RequestNewDataOffering request);

    @PutMapping("/update-offering")
    ResponseEntity<Mono<DataOfferingDto>> updateDataOffering(@Valid @NotNull @RequestBody final DataOfferingDto request);

    @GetMapping("/offering/{id}/offeringId")
    ResponseEntity<Mono<DataOfferingDto>> getDataOfferingById(@PathVariable(name = "id") final String id);

    //    @DeleteMapping("/data-offering/delete/all")
    ResponseEntity<Mono<Void>> deleteAllOffering();

    @GetMapping("/offerings-list")
    ResponseEntity<Flux<OfferingIdResponse>> getOfferingList(@RequestParam(value = "page", defaultValue = "0") final int page,
                                                             @RequestParam(value = "size", defaultValue = "5") final int size);

    @GetMapping("/offering/{id}/providerId")
    ResponseEntity<Flux<DataOfferingDto>> getOfferingByProvider(@PathVariable(name = "id") final String provider,
                                                                @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                @RequestParam(value = "size", defaultValue = "5") final int size);

    @GetMapping("/offering/{category}")
    ResponseEntity<Flux<DataOfferingDto>> getOfferingByCategory(@PathVariable(name = "category") final String category,
                                                                @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                @RequestParam(value = "size", defaultValue = "5") final int size);

    @GetMapping("/contract-parameter/{offeringId}/offeringId")
    ResponseEntity<Mono<ContractParametersResponse>> getContractParameterByOfferingId(@PathVariable(name = "offeringId") final String offeringId);

    @GetMapping("/providers/{category}/category")
    ResponseEntity<Flux<ProviderIdResponse>> getProviderListByCategory(@PathVariable(name = "category") final String category,
                                                                       @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                       @RequestParam(value = "size", defaultValue = "5") final int size);


    @GetMapping("/offerings")
    ResponseEntity<Mono<TotalOfferingResponse>> getOfferingByProviderIdAndCategorySorted(@RequestParam(value = "providerId", defaultValue = "All") String providerId,
                                                                                         @RequestParam(value = "category", defaultValue = "All") String category,
                                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                         @RequestParam(value = "size", defaultValue = "5") int size,
                                                                                         @RequestParam(value = "orderBy", defaultValue = "time") String orderBy,
                                                                                         @RequestParam(value = "sortBy", defaultValue = "desc") String sortBy

    ) throws ExecutionException, InterruptedException;


    @DeleteMapping("/delete-offering/{offeringId}")
    ResponseEntity<Mono<Void>> deleteDataOfferingById(@PathVariable(name = "offeringId") final String offeringId);

}
