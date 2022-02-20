package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class DatasetInformation implements Serializable {

    String measurementType;

    String measurementChannelType;

    String sensorId;

    String deviceId;

    String cppType;

    String sensorType;

}
