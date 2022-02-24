package com.i3market.semanticengine.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDataset {

    @NotNull(message = "title is required")
    @NotBlank(message = "title is required")
    @NotEmpty(message = "title is required")
    @Schema(example = "required", required = true, type = "String")
    String title;

    @NotNull(message = "keyword is required")
    @NotBlank(message = "keyword is required")
    @NotEmpty(message = "keyword is required")
    @Schema(example = "required", required = true, type = "String")
    String keyword;

    @NotNull(message = "dataset is required")
    @NotBlank(message = "dataset is required")
    @NotEmpty(message = "dataset is required")
    @Schema(example = "required", required = true, type = "String")
    String dataset;

    @NotNull(message = "description is required")
    @NotBlank(message = "description is required")
    @NotEmpty(message = "description is required")
    @Schema(example = "required", required = true, type = "String")
    String description;

    @NotNull(message = "issued is required")
    @NotBlank(message = "issued is required")
    @NotEmpty(message = "issued is required")
    @Schema(example = "required", required = true, type = "String")
    String issued;

    String modified;

    @NotNull(message = "temporal is required")
    @NotBlank(message = "temporal is required")
    @NotEmpty(message = "temporal is required")
    @Schema(example = "required", required = true, type = "String")
    String temporal;

    @Schema(example = "english", required = true, type = "String")
    String language;

    @NotNull(message = "spatial is required")
    @NotBlank(message = "spatial is required")
    @NotEmpty(message = "spatial is required")
    @Schema(example = "required", required = true, type = "String")
    String spatial;

    @NotNull(message = "accrualPeriodicity is required")
    @NotBlank(message = "accrualPeriodicity is required")
    @NotEmpty(message = "accrualPeriodicity is required")
    @Schema(example = "required", required = true, type = "String")
    String accrualPeriodicity;

    @NotNull(message = "temporalResolution is required")
    @NotBlank(message = "temporalResolution is required")
    @NotEmpty(message = "temporalResolution is required")
    @Schema(example = "required", required = true, type = "String")
    String temporalResolution;

    List<String> theme = new ArrayList<>();

    @NotNull(message = "distribution is required")
    List<RequestDistribution> distribution = new ArrayList<>();

    List<RequestDatasetInformation> datasetInformation = new ArrayList<>();
}
