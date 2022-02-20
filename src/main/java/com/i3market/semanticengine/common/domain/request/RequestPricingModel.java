package com.i3market.semanticengine.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestPricingModel {

    @NotNull(message = "pricingModelName is required")
    @NotBlank(message = "pricingModelName is required")
    @NotEmpty(message = "pricingModelName is required")
    @Schema(example = "required", required = true, type = "String")
    String pricingModelName;

    @NotNull(message = "basicPrice is required")
    @NotBlank(message = "basicPrice is required")
    @NotEmpty(message = "basicPrice is required")
    BigDecimal basicPrice;

    @NotNull(message = "currency is required")
    @NotBlank(message = "currency is required")
    @NotEmpty(message = "currency is required")
    @Schema(example = "EUR", required = true, type = "String")
    String currency;

    RequestPaymentOnSubscription hasPaymentOnSubscription = RequestPaymentOnSubscription.builder().build();

    RequestPaymentOnApi hasPaymentOnApi = RequestPaymentOnApi.builder().build();

    RequestPaymentOnUnit hasPaymentOnUnit = RequestPaymentOnUnit.builder().build();

    RequestPaymentOnSize hasPaymentOnSize = RequestPaymentOnSize.builder().build();

    RequestFreePrice hasFreePrice = RequestFreePrice.builder().build();
}
