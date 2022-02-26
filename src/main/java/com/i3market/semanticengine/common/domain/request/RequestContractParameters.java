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
public class RequestContractParameters {

    @NotNull(message = "interestOfProvider is required")
    @NotBlank(message = "interestOfProvider is required")
    @NotEmpty(message = "interestOfProvider is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String interestOfProvider = "required";

    @NotNull(message = "interestDescription is required")
    @NotBlank(message = "interestDescription is required")
    @NotEmpty(message = "interestDescription is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String interestDescription = "required";

    @NotNull(message = "hasGoverningJurisdiction is required")
    @NotBlank(message = "hasGoverningJurisdiction is required")
    @NotEmpty(message = "hasGoverningJurisdiction is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String hasGoverningJurisdiction = "required";

    @NotNull(message = "purpose is required")
    @NotBlank(message = "purpose is required")
    @NotEmpty(message = "purpose is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String purpose = "required";

    @NotNull(message = "purposeDescription is required")
    @NotBlank(message = "purposeDescription is required")
    @NotEmpty(message = "purposeDescription is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String purposeDescription = "required";

    RequestIntendedUse hasIntendedUse;

    RequestLicenseGrant hasLicenseGrant;
}
