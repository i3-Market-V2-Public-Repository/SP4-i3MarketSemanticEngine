package com.i3market.semanticengine.common.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDataExchangeSpec {
    @NotNull(message = "encAlg is required")
    @NotBlank(message = "encAlg is required")
    @NotEmpty(message = "encAlg is required")
    @Schema(example = "required", required = true, type = "String")
     String encAlg;

    @NotNull(message = "signingAlg is required")
    @NotBlank(message = "signingAlg is required")
    @NotEmpty(message = "signingAlg is required")
    @Schema(example = "required", required = true, type = "String")
     String signingAlg;

    @NotNull(message = "hashAlg is required")
    @NotBlank(message = "hashAlg is required")
    @NotEmpty(message = "hashAlg is required")
    @Schema(example = "required", required = true, type = "String")
     String hashAlg;

    @NotNull(message = "ledgerContractAddress is required")
    @NotBlank(message = "ledgerContractAddress is required")
    @NotEmpty(message = "ledgerContractAddress is required")
    @Schema(example = "required", required = true, type = "String")
     String ledgerContractAddress;

    @NotNull(message = "ledgerSignerAddress is required")
    @NotBlank(message = "ledgerSignerAddress is required")
    @NotEmpty(message = "ledgerSignerAddress is required")
    @Schema(example = "required", required = true, type = "String")
     String ledgerSignerAddress;


     int pooToPorDelay;
     int pooToPopDelay;
    int pooToSecretDelay;

}
