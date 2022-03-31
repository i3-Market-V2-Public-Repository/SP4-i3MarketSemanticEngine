package com.i3market.semanticengine.common.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = false)
@JsonDeserialize(builder = OfferingGeneralContext.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferingGeneralContext {

    @Schema(example = "http://i3-market.eu/backplane/core/", required = true, type = "String")
    @lombok.Builder.Default
    String core = "http://i3-market.eu/backplane/core/";

    @Schema(example = "http://www.w3.org/ns/dcat#", required = true, type = "String")
    @lombok.Builder.Default
    String dcat = "http://www.w3.org/ns/dcat#";

    @Schema(example = "http://i3-market.eu/backplane/pricingmodel", required = true, type = "String")
    @lombok.Builder.Default
    String pricingModel = "http://i3-market.eu/backplane/pricingmodel";
}
