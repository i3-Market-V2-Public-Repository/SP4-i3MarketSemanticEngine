package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
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
