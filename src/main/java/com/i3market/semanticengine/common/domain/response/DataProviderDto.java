package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = DataProviderDto.Builder.class)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataProviderDto {

    @Hidden
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String id;

    String providerId;

    String name;

    @lombok.Builder.Default
    String description = null;

    List<OrganizationDto> organization;

    String createdAt;

    String updatedAt;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }
}
