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
public class RequestLicenseGrant {


    @ToString.Include
    @NotNull(message = "transferable is required")
    @NotBlank(message = "transferable is required")
    @NotEmpty(message = "transferable is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean transferable;

    @ToString.Include
    @NotNull(message = "exclusiveness is required")
    @NotBlank(message = "exclusiveness is required")
    @NotEmpty(message = "exclusiveness is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean exclusiveness;

    @ToString.Include
    @NotNull(message = "paidUp is required")
    @NotBlank(message = "paidUp is required")
    @NotEmpty(message = "paidUp is required")
    @Schema(example = "true", required = true, type = "boolean")
    boolean paidUp;
    @ToString.Include
    @NotNull(message = "revocable is required")
    @NotBlank(message = "revocable is required")
    @NotEmpty(message = "revocable is required")
    @Schema(example = "true", required = true, type = "boolean")
    boolean revocable;

    @ToString.Include
    @NotNull(message = "processing is required")
    @NotBlank(message = "processing is required")
    @NotEmpty(message = "processing is required")
    @Schema(example = "true", required = true, type = "boolean")
    boolean  processing;

    @ToString.Include
    @NotNull(message = "modifying is required")
    @NotBlank(message = "modifying is required")
    @NotEmpty(message = "modifying is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean  modifying;

    @ToString.Include
    @NotNull(message = "analyzing is required")
    @NotBlank(message = "analyzing is required")
    @NotEmpty(message = "analyzing is required")
    @Schema(example = "true", required = true, type = "boolean")
    boolean analyzing;

    @ToString.Include
    @NotNull(message = "storingData is required")
    @NotBlank(message = "storingData is required")
    @NotEmpty(message = "storingData is required")
    @Schema(example = "true", required = true, type = "boolean")
    boolean storingData;

    @ToString.Include
    @NotNull(message = "storingCopy is required")
    @NotBlank(message = "storingCopy is required")
    @NotEmpty(message = "storingCopy is required")
    @Schema(example = "true", required = true, type = "boolean")
    boolean storingCopy;

    @ToString.Include
    @NotNull(message = "reproducing is required")
    @NotBlank(message = "reproducing is required")
    @NotEmpty(message = "reproducing is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean  reproducing;

    @ToString.Include
    @NotNull(message = "distributing is required")
    @NotBlank(message = "distributing is required")
    @NotEmpty(message = "distributing is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean distributing;

    @ToString.Include
    @NotNull(message = "loaning is required")
    @NotBlank(message = "loaning is required")
    @NotEmpty(message = "loaning is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean  loaning;

    @ToString.Include
    @NotNull(message = "selling is required")
    @NotBlank(message = "selling is required")
    @NotEmpty(message = "selling is required")
    @Schema(example = "true", required = true, type = "boolean")
    boolean selling;

    @ToString.Include
    @NotNull(message = "renting is required")
    @NotBlank(message = "renting is required")
    @NotEmpty(message = "renting is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean  renting;

    @ToString.Include
    @NotNull(message = "furtherLicensing is required")
    @NotBlank(message = "furtherLicensing is required")
    @NotEmpty(message = "furtherLicensing is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean furtherLicensing;

    @ToString.Include
    @NotNull(message = "leasing is required")
    @NotBlank(message = "leasing is required")
    @NotEmpty(message = "leasing is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean leasing;

}
