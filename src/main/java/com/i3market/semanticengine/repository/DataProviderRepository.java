package com.i3market.semanticengine.repository;

import com.i3market.semanticengine.common.domain.DataProviderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DataProviderRepository extends ReactiveCrudRepository<DataProviderEntity, String> {

    Mono<DataProviderEntity> findByProviderId(String providerId);

}
