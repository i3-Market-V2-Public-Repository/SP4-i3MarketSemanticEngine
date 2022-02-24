package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = PricingModelDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingModelDto {

    @lombok.Builder.Default
    @JsonIgnore
    String type = "http://i3-market.eu/backplane/pricingmodel/PricingModel";

    String pricingModelName;

    BigDecimal basicPrice;

    String currency;

    PaymentOnSubscriptionDto hasPaymentOnSubscription;

    PaymentOnApiDto hasPaymentOnApi;

    PaymentOnUnitDto hasPaymentOnUnit;

    PaymentOnSizeDto hasPaymentOnSize;

    FreePriceDto hasFreePrice;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }

}
