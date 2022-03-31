package com.i3market.semanticengine.repository;

import com.i3market.semanticengine.common.domain.entity.DataProvider;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DataProviderRepository extends ReactiveCrudRepository<DataProvider, String> {

    Mono<DataProvider> findByProviderId(final String providerId);

}
