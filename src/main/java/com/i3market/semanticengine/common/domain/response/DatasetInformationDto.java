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
@JsonDeserialize(builder = DatasetInformationDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetInformationDto {

    @lombok.Builder.Default
    @JsonIgnore
    String type = "http://i3-market.eu/backplane/core/DatasetInformation";

    String measurementType;

    String measurementChannelType;

    String sensorId;

    String deviceId;

    String cppType;

    String sensorType;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }

}
