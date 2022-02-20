package com.i3market.semanticengine.common.domain.request;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class RequestPaymentOnUnit {

    String paymentOnUnitName;

    String description;

    Integer dataUnit;

    BigDecimal hasUnitPrice;

}
