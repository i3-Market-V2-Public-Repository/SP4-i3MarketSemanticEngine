package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = ProviderIdResponse.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderIdResponse {

    String provider;

}
