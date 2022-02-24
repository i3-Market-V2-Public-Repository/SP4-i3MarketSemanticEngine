package com.i3market.semanticengine.repository;

import com.i3market.semanticengine.common.domain.entity.DataOffering;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DataOfferingRepository extends ReactiveCrudRepository<DataOffering, String> {

    Flux<DataOffering> findByProvider(final String provider);

    Flux<DataOffering> findByCategory(final String category);

}
