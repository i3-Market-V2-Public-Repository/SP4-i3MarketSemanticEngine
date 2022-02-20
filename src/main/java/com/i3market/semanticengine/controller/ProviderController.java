package com.i3market.semanticengine.controller;

import com.i3market.semanticengine.common.domain.DataProviderDto;
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

    @PostMapping("/provider")
    ResponseEntity<Mono<DataProviderDto>> createDataProvider(@Valid @NotNull @RequestBody final DataProviderDto dataProviderDto);

    @PutMapping("/provider")
    ResponseEntity<Mono<DataProviderDto>> updatedDataProvider(@Valid @NotNull @RequestBody final DataProviderDto dataProviderDto);

    @GetMapping("/provider/{providerId}")
    ResponseEntity<Mono<DataProviderDto>> getDataProviderByProviderId(@PathVariable(name = "providerId") final String providerId);

    @DeleteMapping("/provider")
    ResponseEntity<Mono<Void>> deleteAllProvider();

    @DeleteMapping("/provider/{providerId}")
    ResponseEntity<Mono<Void>> deleteProviderByProviderId(@PathVariable(name = "providerId") final String providerId);

    @GetMapping("/provider/list")
    ResponseEntity<Flux<DataProviderDto>> providerList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "5") int size);
}
