package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = DistributionDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistributionDto {

    @lombok.Builder.Default
    String type = "http://www.w3.org/ns/dcat#Distribution";

    String title;

    String description;

    String license;

    String accessRights;

    String downloadType;

    String conformsTo;

    String mediaType;

    String packageFormat;

    AccessServiceDto accessService;
}
