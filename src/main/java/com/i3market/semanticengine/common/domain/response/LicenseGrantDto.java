package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = LicenseGrantDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicenseGrantDto {

    @lombok.Builder.Default
    String type = "http://i3-market.eu/backplane/core/LicenseGrant";

    boolean copyData;

    boolean transferable;

    boolean exclusiveness;

    boolean revocable;
}
