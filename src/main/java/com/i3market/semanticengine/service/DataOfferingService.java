package com.i3market.semanticengine.service;

import com.i3market.semanticengine.common.domain.CategoriesList;
import com.i3market.semanticengine.common.domain.request.RequestDataOffering;
import com.i3market.semanticengine.common.domain.response.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public interface DataOfferingService {

    Flux<CategoriesList> getCategoryList(ServerHttpRequest serverHttpRequest);

    Mono<DataOfferingDto> createDataOffering(final RequestDataOffering dto, ServerHttpRequest serverHttpRequest) throws ExecutionException, InterruptedException;

    Mono<Void> deleteAllOffering();

    Mono<DataOfferingDto> getDataOfferingById(final String id);

    Flux<OfferingIdResponse> getOfferingList(final int page, final int size);

    Flux<OfferingIdResponse> getOfferingListonActive(final int page, final int size);

    Flux<OfferingIdResponse> getOfferingListonSharedNetwork(final int page, final int size);

    Mono<DataOfferingDto> updateDataOffering(final DataOfferingDto request);

    Flux<DataOfferingDto> getOfferingByProvider(String provider, int page, int size);

    Flux<DataOfferingDto> getOfferingByCategory(final String category, final int page, final int size);

    Flux<DataOfferingDto> getByDataOfferingTitleAndPricingModelName(String dataOfferingTitle , String pricingModelName , final int page , final int size);

    Mono<ContractParametersResponse> getContractParameterByOfferingId(final String offeringId);

    Flux<ProviderIdResponse> getProviderListByCategory(String category, int page, int size);

    Mono<TotalOfferingResponse> getOfferingByProviderIdAndCategorySorted(final String providerId, final String category, final int page,
                                                                         final int size, final String orderBy, final String sortBy);

    Mono<Void> deleteDataOfferingById(final String offeringId);

     void deleteAll();
    Flux<DataOfferingDto> getByActiveAndShareDataWithThirdParty(boolean active , boolean shareDataWithThirdParty);
    Flux<DataOfferingDto> getBySharedNetAndTransferableAndFreePrice(boolean shared , boolean transfer, boolean freePrice);
    String getOfferingTemplate();

    Flux<DataOfferingDto> getOfferingsByActiveAndCategory(final String category , final int page, final int size);

    Flux<DataOfferingDto> getOfferingsByActiveAndProvider(final String provider , final int page, final int size);
}
