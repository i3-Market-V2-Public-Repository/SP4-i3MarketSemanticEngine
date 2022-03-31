package com.i3market.semanticengine.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = CategoriesList.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoriesList {

    String name;

    String description;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {
    }
}
