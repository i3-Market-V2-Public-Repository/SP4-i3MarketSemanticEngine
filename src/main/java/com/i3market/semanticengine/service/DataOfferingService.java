package com.i3market.semanticengine.service;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.DataOfferingDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataOfferingService {

    Flux<CategoriesList> getCategoryList();

    Mono<DataOfferingDto> createDataOffering(RequestNewDataOffering dto);

    Mono<Void> deleteAllOffering();

    Mono<DataOfferingDto> getDataOfferingById(String id);
}
