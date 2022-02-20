package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = PricingModelDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingModelDto {

    @lombok.Builder.Default
    String type = "http://i3-market.eu/backplane/pricingmodel/PricingModel";

    String pricingModelName;

    BigDecimal basicPrice;

    String currency;

    PaymentOnSubscriptionDto hasPaymentOnSubscription;

    PaymentOnApiDto hasPaymentOnApi;

    PaymentOnUnitDto hasPaymentOnUnit;

    PaymentOnSizeDto hasPaymentOnSize;

    FreePriceDto hasFreePrice;

}
