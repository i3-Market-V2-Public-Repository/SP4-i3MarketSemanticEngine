package com.i3market.semanticengine.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDatasetInformation {

    String measurementType;

    String measurementChannelType;

    String sensorId;

    String deviceId;

    String cppType;

    String sensorType;
}
