package com.i3market.semanticengine.controller.impl;

import com.i3market.semanticengine.common.domain.request.RequestDataProvider;
import com.i3market.semanticengine.common.domain.response.DataProviderDto;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import com.i3market.semanticengine.controller.ProviderController;
import com.i3market.semanticengine.service.DataProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ProviderControllerImpl implements ProviderController {

    private final DataProviderService dataProviderService;


    @Override
    public ResponseEntity<Mono<DataProviderDto>> createDataProvider(final RequestDataProvider dto) {
        log.info("Start creating data provider");
        final Mono<DataProviderDto> provider = dataProviderService.createDataProvider(dto);
        log.info("Start creating data provider, success");

        return ResponseEntity.ok(provider);

    }

    @Override
    public ResponseEntity<Mono<DataProviderDto>> updatedDataProvider(final RequestDataProvider dataProviderDto) {
        log.info("Start updating data provider");
        final Mono<DataProviderDto> updatedProvider = dataProviderService.updatedDataProvider(dataProviderDto);
        log.info("Start updating data provider, success");
        return ResponseEntity.ok(updatedProvider);
    }

    @Override
    public ResponseEntity<Mono<DataProviderDto>> getDataProviderByProviderId(final String providerId) {
        log.info("GET data provider by providerId");
        return ResponseEntity.ok(dataProviderService.getDataProviderByProviderId(providerId));
    }

    @Override
    public ResponseEntity<Mono<Void>> deleteProviderByProviderId(final String providerId) {
        log.info("DELETE provider by providerId");
        return ResponseEntity.ok(dataProviderService.deleteProviderByProviderId(providerId));
    }

    @Override
    public ResponseEntity<Flux<ProviderIdResponse>> providerList(final int page, final int size) {
        log.info("GET provider list");
        return ResponseEntity.ok(dataProviderService.providerList(page, size));
    }

}
