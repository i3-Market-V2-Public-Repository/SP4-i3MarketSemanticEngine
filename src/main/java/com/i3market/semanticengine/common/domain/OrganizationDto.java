package com.i3market.semanticengine.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = OrganizationDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDto {

    String organizationId;

    String name;

    String description;

    String address;

    String contactPoint;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }
}
