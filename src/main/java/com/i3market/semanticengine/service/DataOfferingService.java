package com.i3market.semanticengine.service;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestNewDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface DataOfferingService {

    Flux<CategoriesList> getCategoryList();

    Mono<DataOfferingDto> createDataOffering(final RequestNewDataOffering dto) throws ExecutionException, InterruptedException;

    Mono<Void> deleteAllOffering();

    Mono<DataOfferingDto> getDataOfferingById(final String id);

    Flux<OfferingIdResponse> getOfferingList(final int page, final int size);

    Mono<DataOfferingDto> updateDataOffering(final DataOfferingDto request);

    Flux<DataOfferingDto> getOfferingByProvider(String provider, int page, int size);

    Flux<DataOfferingDto> getOfferingByCategory(final String category, final int page, final int size);

    Mono<ContractParametersResponse> getContractParameterByOfferingId(final String offeringId);

    Flux<ProviderIdResponse> getProviderListByCategory(String category, int page, int size);

    Mono<TotalOfferingResponse> getOfferingByProviderIdAndCategorySorted(final String providerId, final String category, final int page,
                                                                         final int size, final String orderBy, final String sortBy) throws ExecutionException, InterruptedException;

    Mono<Void> deleteDataOfferingById(final String offeringId);

    String getOfferingTemplate();
}
