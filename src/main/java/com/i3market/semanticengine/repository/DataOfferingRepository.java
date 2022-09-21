package com.i3market.semanticengine.repository;

import com.i3market.semanticengine.common.domain.entity.DataOffering;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DataOfferingRepository extends ReactiveCrudRepository<DataOffering, String> {

    Flux<DataOffering> findByProvider(final String provider);

    Flux<DataOffering> findByCategory(final String category);

    Flux<DataOffering> findByDataOfferingTitleOrHasPricingModelPricingModelName(final String dataOfferingTitle , final String pricingModelName);

    Flux<DataOffering> findByActiveAndContractParametersHasIntendedUseShareDataWithThirdParty
            (final boolean active , final boolean shareDataWithThirdParty);
    Flux<DataOffering>
            findByContractParametersHasIntendedUseShareDataWithThirdPartyAndContractParametersHasLicenseGrantTransferableAndHasPricingModelHasFreePriceHasPriceFree(
                    final boolean shareDataWithThirdParty, final boolean transferable , final boolean hasFreePrice
    );

}
