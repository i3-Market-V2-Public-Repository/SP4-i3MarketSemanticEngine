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
    @NotNull(message = "copyData is required")
    @NotBlank(message = "copyData is required")
    @NotEmpty(message = "copyData is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean copyData;

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
    @NotNull(message = "revocable is required")
    @NotBlank(message = "revocable is required")
    @NotEmpty(message = "revocable is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean revocable;
}
