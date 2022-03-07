package com.i3market.semanticengine.controller;

import com.i3market.semanticengine.common.domain.request.RequestDataProvider;
import com.i3market.semanticengine.common.domain.response.DataProviderDto;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.i3market.semanticengine.common.constant.Constants.SEMANTIC_ENGINE_MAIN_PATH;

@RequestMapping(SEMANTIC_ENGINE_MAIN_PATH)
@Tag(name = "Data Provider API")
public interface ProviderController {

    @PostMapping()
    @Operation(summary = "POST create a new provider")
    ResponseEntity<Mono<DataProviderDto>> createDataProvider(@Valid @NotNull @RequestBody final RequestDataProvider dataProviderDto);

    @PutMapping("/update/provider")
    @Operation(summary = "UPDATE an existing provider")
    ResponseEntity<Mono<DataProviderDto>> updatedDataProvider(@Valid @NotNull @RequestBody final RequestDataProvider dataProviderDto);

    @GetMapping("/offering/provider/{providerId}")
    @Operation(summary = "GET a provider by providerId")
    ResponseEntity<Mono<DataProviderDto>> getDataProviderByProviderId(@PathVariable(name = "providerId") final String providerId);

    @DeleteMapping("/provider/{providerId}/delete")
    @Operation(summary = "DELETE a provider by providerId")
    ResponseEntity<Mono<Void>> deleteProviderByProviderId(@PathVariable(name = "providerId") final String providerId);

    @GetMapping("/providers-list")
    @Operation(summary = "GET a list of providers")
    ResponseEntity<Flux<ProviderIdResponse>> providerList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "5") int size);
}
