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
public class RequestIntendedUse {

    @NotNull(message = "processData is required")
    @NotBlank(message = "processData is required")
    @NotEmpty(message = "processData is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean processData;

    @NotNull(message = "shareDataWithThirdParty is required")
    @NotBlank(message = "shareDataWithThirdParty is required")
    @NotEmpty(message = "shareDataWithThirdParty is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean shareDataWithThirdParty = true;

    @NotNull(message = "editData is required")
    @NotBlank(message = "editData is required")
    @NotEmpty(message = "editData is required")
    @Schema(example = "false", required = true, type = "boolean")
    boolean editData = false;
}
