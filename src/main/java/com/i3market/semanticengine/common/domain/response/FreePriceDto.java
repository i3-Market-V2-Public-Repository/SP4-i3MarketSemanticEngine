package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = FreePriceDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreePriceDto {

    @lombok.Builder.Default
    String type = "http://i3-market.eu/backplane/pricingmodel/FreePrice";

    boolean hasPriceFree;

}
