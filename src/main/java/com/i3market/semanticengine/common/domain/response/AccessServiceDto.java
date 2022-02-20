package com.i3market.semanticengine.common.domain.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = AccessServiceDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessServiceDto {

    @lombok.Builder.Default
    String type = "http://www.w3.org/ns/dcat#DataService";

    String conformsTo;

    String endpointDescription;

    String endpointURL;

    String servesDataset;

    String serviceSpecs;
}
