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
@JsonDeserialize(builder = DistributionDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistributionDto {

    @lombok.Builder.Default
    @JsonIgnore
    String type = "http://www.w3.org/ns/dcat#Distribution";

    String title;

    String description;

    String license;

    String accessRights;

    String downloadType;

    String conformsTo;

    String mediaType;

    String packageFormat;

    boolean dataStream;

    AccessServiceDto accessService;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }
}
