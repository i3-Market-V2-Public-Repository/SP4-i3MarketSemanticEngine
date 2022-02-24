package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = DatasetDto.Builder.class)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetDto {

    @lombok.Builder.Default
    @JsonIgnore
    String type = "http://www.w3.org/ns/dcat#Dataset";

    String title;

    String keyword;

    String dataset;

    String description;

    String issued;

    String modified;

    String temporal;

    String language;

    String spatial;

    String accrualPeriodicity;

    String temporalResolution;

    List<String> theme;

    List<DistributionDto> distribution;

    List<DatasetInformationDto> datasetInformation;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }
}
