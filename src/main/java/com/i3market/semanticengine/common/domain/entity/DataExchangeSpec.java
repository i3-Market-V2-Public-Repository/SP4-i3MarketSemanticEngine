package com.i3market.semanticengine.common.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataExchangeSpec implements Serializable {
    private String encAlg;
    private String signingAlg;
    private String hashAlg;
    private String ledgerContractAddress;
    private String ledgerSignerAddress;
    private int pooToPorDelay;
    private int pooToPopDelay;
    private int pooToSecretDelay;

}
