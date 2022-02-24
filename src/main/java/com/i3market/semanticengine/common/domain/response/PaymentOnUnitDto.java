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
@JsonDeserialize(builder = PaymentOnUnitDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentOnUnitDto {

    @lombok.Builder.Default
    @JsonIgnore
    String type = "http://i3-market.eu/backplane/pricingmodel/PaymentOnUnit";

    String paymentOnUnitName;

    String description;

    Integer dataUnit;

    BigDecimal hasUnitPrice;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }
}
