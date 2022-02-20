package com.i3market.semanticengine.service;

import com.i3market.semanticengine.common.domain.DataProviderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataProviderService {

    Mono<DataProviderDto> createDataProvider(final DataProviderDto dto);

    Mono<DataProviderDto> getDataProviderByProviderId(final String providerId);

    Mono<Void> deleteAllProvider();

    Mono<Void> deleteProviderByProviderId(final String providerId);

    Mono<DataProviderDto> updatedDataProvider(final DataProviderDto dataProviderDto);

    Flux<DataProviderDto> providerList(int page, int size);
    
}
