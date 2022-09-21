package com.i3market.semanticengine.common.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class RequestDistribution {

    @NotNull(message = "title is required")
    @NotBlank(message = "title is required")
    @NotEmpty(message = "title is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String title = "require";

    @NotNull(message = "description is required")
    @NotBlank(message = "description is required")
    @NotEmpty(message = "description is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String description = "require";

    @NotNull(message = "license is required")
    @NotBlank(message = "license is required")
    @NotEmpty(message = "license is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String license = "require";

    @NotNull(message = "accessRights is required")
    @NotBlank(message = "accessRights is required")
    @NotEmpty(message = "accessRights is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String accessRights = "require";

    @NotNull(message = "downloadType is required")
    @NotBlank(message = "downloadType is required")
    @NotEmpty(message = "downloadType is required")
    @Schema(example = "JSON", required = true, type = "String")
    @lombok.Builder.Default
    String downloadType = "require";

    @NotNull(message = "conformsTo is required")
    @NotBlank(message = "conformsTo is required")
    @NotEmpty(message = "conformsTo is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String conformsTo = "require";

    @NotNull(message = "mediaType is required")
    @NotBlank(message = "mediaType is required")
    @NotEmpty(message = "mediaType is required")
    @Schema(example = "JSON", required = true, type = "String")
    @lombok.Builder.Default
    String mediaType = "required";

    @NotNull(message = "packageFormat is required")
    @NotBlank(message = "packageFormat is required")
    @NotEmpty(message = "packageFormat is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String packageFormat = "require";

    @NotNull(message = "dataStream is required")
    @NotBlank(message = "dataStream is required")
    @NotEmpty(message = "dataStream is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean dataStream=false;

    RequestAccessService accessService;

    RequestDataExchangeSpec dataExchangeSpec;
}
