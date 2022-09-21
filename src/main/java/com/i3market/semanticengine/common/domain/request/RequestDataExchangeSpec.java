package com.i3market.semanticengine.common.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDataExchangeSpec {
    private String encAlg;
    private String signingAlg;
    private String hashAlg;
    private String ledgerContractAddress;
    private String ledgerSignerAddress;
    private int pooToPorDelay;
    private int pooToPopDelay;
    private int pooToSecretDelay;

}
