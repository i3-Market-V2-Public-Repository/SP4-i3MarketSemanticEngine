package com.i3market.semanticengine.repository;

import com.i3market.semanticengine.common.domain.entity.DataOffering;
import org.springframework.data.repository.CrudRepository;

public interface OfferingRepository extends CrudRepository<DataOffering, String> {
}
