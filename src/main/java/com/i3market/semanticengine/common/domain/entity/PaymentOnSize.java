package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class PaymentOnSize implements Serializable {

    String paymentOnSizeName;

    String description;

    String dataSize;

    BigDecimal hasSizePrice;

}
