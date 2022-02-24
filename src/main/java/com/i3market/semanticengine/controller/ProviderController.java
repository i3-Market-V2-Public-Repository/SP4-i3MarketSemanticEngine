package com.i3market.semanticengine.controller;

import com.i3market.semanticengine.common.domain.request.RequestDataProvider;
import com.i3market.semanticengine.common.domain.response.DataProviderDto;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
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

    @PostMapping("")
    ResponseEntity<Mono<DataProviderDto>> createDataProvider(@Valid @NotNull @RequestBody final RequestDataProvider dataProviderDto);

    @PutMapping("/update/provider")
    ResponseEntity<Mono<DataProviderDto>> updatedDataProvider(@Valid @NotNull @RequestBody final RequestDataProvider dataProviderDto);

    @GetMapping("/offerings/provider/{providerId}")
    ResponseEntity<Mono<DataProviderDto>> getDataProviderByProviderId(@PathVariable(name = "providerId") final String providerId);

    //    @DeleteMapping("/provider")
    ResponseEntity<Mono<Void>> deleteAllProvider();

    @DeleteMapping("/provider/{providerId}")
    ResponseEntity<Mono<Void>> deleteProviderByProviderId(@PathVariable(name = "providerId") final String providerId);

    @GetMapping("/provider-list")
    ResponseEntity<Flux<ProviderIdResponse>> providerList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "5") int size);
}
