package com.i3market.semanticengine.controller;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.i3market.semanticengine.common.constant.Constants.SEMANTIC_ENGINE_MAIN_PATH;

@RequestMapping(SEMANTIC_ENGINE_MAIN_PATH)
@Tag(name = "Data Offering API")
public interface DataOfferingController {

    @GetMapping("/categories-list")
    @Operation(summary = "GET a list of category")
    ResponseEntity<Flux<CategoriesList>> getCategoryList();

    @PostMapping("/data-offering")
    @Operation(summary = "POST register a new offering")
    ResponseEntity<Mono<DataOfferingDto>> createDataOffering(@Valid @NotNull @RequestBody final RequestDataOffering request) throws ExecutionException, InterruptedException;

    @PutMapping("/update-offering")
    @Operation(summary = "UPDATE an existing offering")
    ResponseEntity<Mono<DataOfferingDto>> updateDataOffering(@Valid @NotNull @RequestBody final DataOfferingDto request);

    @GetMapping("/offering/{id}/offeringId")
    @Operation(summary = "GET an offering by offeringId")
    ResponseEntity<Mono<DataOfferingDto>> getDataOfferingById(@PathVariable(name = "id") final String id);

    //    @DeleteMapping("/data-offering/delete/all")
    @Operation(summary = "DELETE all offerings")
    ResponseEntity<Mono<Void>> deleteAllOffering();

    @GetMapping("/offerings-list")
    @Operation(summary = "GET a list of all offering")
    ResponseEntity<Flux<OfferingIdResponse>> getOfferingList(@RequestParam(value = "page", defaultValue = "0") final int page,
                                                             @RequestParam(value = "size", defaultValue = "5") final int size);

    @GetMapping("/offering/{id}/providerId")
    @Operation(summary = "GET an a list of offering for a provider")
    ResponseEntity<Flux<DataOfferingDto>> getOfferingByProvider(@PathVariable(name = "id") final String provider,
                                                                @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                @RequestParam(value = "size", defaultValue = "5") final int size);

    @GetMapping("/offering/{category}")
    @Operation(summary = "GET a list of offerings by category")
    ResponseEntity<Flux<DataOfferingDto>> getOfferingByCategory(@PathVariable(name = "category") final String category,
                                                                @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                @RequestParam(value = "size", defaultValue = "5") final int size);

    @GetMapping("/contract-parameter/{offeringId}/offeringId")
    @Operation(summary = "GET a contract parameter by offeringId")
    ResponseEntity<Mono<ContractParametersResponse>> getContractParameterByOfferingId(@PathVariable(name = "offeringId") final String offeringId);

    @GetMapping("/providers/{category}/category")
    @Operation(summary = "GET a list of provider by category")
    ResponseEntity<Flux<ProviderIdResponse>> getProviderListByCategory(@PathVariable(name = "category") final String category,
                                                                       @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                       @RequestParam(value = "size", defaultValue = "5") final int size);

    @GetMapping("/offerings")
    @Operation(
            summary = "GET a total of offering and offering list",
            description = "${api.i3market-semantic-engine.get-total-offering.notes}")
    @SneakyThrows
    ResponseEntity<Mono<TotalOfferingResponse>> getOfferingByProviderIdAndCategorySorted(@RequestParam(value = "providerId", defaultValue = "All") String providerId,
                                                                                         @RequestParam(value = "category", defaultValue = "All") String category,
                                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                         @RequestParam(value = "size", defaultValue = "5") int size,
                                                                                         @RequestParam(value = "orderBy", defaultValue = "time") String orderBy,
                                                                                         @RequestParam(value = "sortBy", defaultValue = "desc") String sortBy
    );


    @DeleteMapping("/delete-offering/{id}")
    @Operation(summary = "DELETE an offering by offeringId")
    ResponseEntity<Mono<Void>> deleteDataOfferingById(@PathVariable(name = "id") final String offeringId);

    @GetMapping(value = "/offering/offering-template")
    @Operation(summary = "Download offering template")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "internal server error to create template"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    @SneakyThrows
    ResponseEntity<String> getOfferingTemplate() throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException;

}
