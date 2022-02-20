package com.i3market.semanticengine.common.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class RequestFreePrice {

    @Schema(example = "false", required = true, type = "boolean")
    boolean hasPriceFree;

}
