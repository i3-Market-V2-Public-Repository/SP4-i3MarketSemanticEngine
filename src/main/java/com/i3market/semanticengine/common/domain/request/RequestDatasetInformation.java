package com.i3market.semanticengine.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDatasetInformation {

    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String measurementType = "require";

    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String measurementChannelType = "require";

    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String sensorId = "require";

    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String deviceId = "require";

    String cppType = "require";

    String sensorType = "require";
}
