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
    String title;

    @NotNull(message = "description is required")
    @NotBlank(message = "description is required")
    @NotEmpty(message = "description is required")
    @Schema(example = "required", required = true, type = "String")
    String description;

    @NotNull(message = "license is required")
    @NotBlank(message = "license is required")
    @NotEmpty(message = "license is required")
    @Schema(example = "required", required = true, type = "String")
    String license;

    @NotNull(message = "accessRights is required")
    @NotBlank(message = "accessRights is required")
    @NotEmpty(message = "accessRights is required")
    @Schema(example = "required", required = true, type = "String")
    String accessRights;

    @NotNull(message = "downloadType is required")
    @NotBlank(message = "downloadType is required")
    @NotEmpty(message = "downloadType is required")
    @Schema(example = "JSON", required = true, type = "String")
    String downloadType;

    @NotNull(message = "conformsTo is required")
    @NotBlank(message = "conformsTo is required")
    @NotEmpty(message = "conformsTo is required")
    @Schema(example = "required", required = true, type = "String")
    String conformsTo;

    @NotNull(message = "mediaType is required")
    @NotBlank(message = "mediaType is required")
    @NotEmpty(message = "mediaType is required")
    @Schema(example = "JSON", required = true, type = "String")
    String mediaType;

    @NotNull(message = "packageFormat is required")
    @NotBlank(message = "packageFormat is required")
    @NotEmpty(message = "packageFormat is required")
    @Schema(example = "required", required = true, type = "String")
    String packageFormat;

    RequestAccessService accessService;
}
