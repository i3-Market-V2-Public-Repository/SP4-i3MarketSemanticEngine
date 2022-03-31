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
public class PricingModel implements Serializable {

    String type;

    String pricingModelName;

    BigDecimal basicPrice;

    String currency;

    PaymentOnSubscription hasPaymentOnSubscription;

    PaymentOnApi hasPaymentOnApi;

    PaymentOnUnit hasPaymentOnUnit;

    PaymentOnSize hasPaymentOnSize;

    FreePrice hasFreePrice;

}
