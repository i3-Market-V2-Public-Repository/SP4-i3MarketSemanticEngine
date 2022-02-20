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
public class PaymentOnUnit implements Serializable {

    String paymentOnUnitName;

    String description;

    Integer dataUnit;

    BigDecimal hasUnitPrice;

}
