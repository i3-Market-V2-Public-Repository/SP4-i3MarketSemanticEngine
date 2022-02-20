package com.i3market.semanticengine.repository;

import com.i3market.semanticengine.common.domain.entity.DataOffering;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DataOfferingRepository extends ReactiveCrudRepository<DataOffering, String> {
}
