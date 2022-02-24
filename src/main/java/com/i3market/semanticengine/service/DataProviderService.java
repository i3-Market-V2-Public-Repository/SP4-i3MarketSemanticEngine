package com.i3market.semanticengine.service;

import com.i3market.semanticengine.common.domain.request.RequestDataProvider;
import com.i3market.semanticengine.common.domain.response.DataProviderDto;
import com.i3market.semanticengine.common.domain.response.ProviderIdResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataProviderService {

    Mono<DataProviderDto> createDataProvider(final RequestDataProvider dto);

    Mono<DataProviderDto> getDataProviderByProviderId(final String providerId);

    Mono<Void> deleteAllProvider();

    Mono<Void> deleteProviderByProviderId(final String providerId);

    Mono<DataProviderDto> updatedDataProvider(final RequestDataProvider dataProviderDto);

    Flux<ProviderIdResponse> providerList(final int page, final int size);

}
