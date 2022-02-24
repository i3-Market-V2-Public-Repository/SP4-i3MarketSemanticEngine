package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = FreePriceDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreePriceDto {

    @lombok.Builder.Default
    @JsonIgnore
    String type = "http://i3-market.eu/backplane/pricingmodel/FreePrice";

    boolean hasPriceFree;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }

}
