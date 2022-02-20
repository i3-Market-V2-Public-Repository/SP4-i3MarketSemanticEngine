package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = DatasetDto.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetDto {

    @lombok.Builder.Default
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

    List<DistributionDto> distribution;

    List<DatasetInformationDto> datasetInformation;
}
