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
public class PaymentOnSubscription implements Serializable {

    String paymentOnSubscriptionName;

    String paymentType;

    String timeDuration;

    String description;

    String repeat;

    BigDecimal hasSubscriptionPrice;

}
