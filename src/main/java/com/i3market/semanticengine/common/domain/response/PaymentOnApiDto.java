package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = PaymentOnApiDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentOnApiDto {

    @lombok.Builder.Default
    String type = "http://i3-market.eu/backplane/pricingmodel/PaymentOnApi";

    String paymentOnApiName;

    String description;

    Integer numberOfObject;

    BigDecimal hasApiPrice;

}
