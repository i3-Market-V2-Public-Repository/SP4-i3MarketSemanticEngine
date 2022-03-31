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
    @lombok.Builder.Default
    String title = "require";

    @NotNull(message = "keyword is required")
    @NotBlank(message = "keyword is required")
    @NotEmpty(message = "keyword is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String keyword = "require";

    @NotNull(message = "dataset is required")
    @NotBlank(message = "dataset is required")
    @NotEmpty(message = "dataset is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String dataset = "require";

    @NotNull(message = "description is required")
    @NotBlank(message = "description is required")
    @NotEmpty(message = "description is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String description = "require";

    @NotNull(message = "issued is required")
    @NotBlank(message = "issued is required")
    @NotEmpty(message = "issued is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String issued = "require";

    @lombok.Builder.Default
    String modified = "require";

    @NotNull(message = "temporal is required")
    @NotBlank(message = "temporal is required")
    @NotEmpty(message = "temporal is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String temporal = "require";

    @Schema(example = "english", required = true, type = "String")
    @lombok.Builder.Default
    String language = "require";

    @NotNull(message = "spatial is required")
    @NotBlank(message = "spatial is required")
    @NotEmpty(message = "spatial is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String spatial = "require";

    @NotNull(message = "accrualPeriodicity is required")
    @NotBlank(message = "accrualPeriodicity is required")
    @NotEmpty(message = "accrualPeriodicity is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String accrualPeriodicity = "require";

    @NotNull(message = "temporalResolution is required")
    @NotBlank(message = "temporalResolution is required")
    @NotEmpty(message = "temporalResolution is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String temporalResolution = "require";

    List<String> theme = new ArrayList<>();

    @NotNull(message = "distribution is required")
    List<RequestDistribution> distribution = new ArrayList<>();

    List<RequestDatasetInformation> datasetInformation = new ArrayList<>();
}
