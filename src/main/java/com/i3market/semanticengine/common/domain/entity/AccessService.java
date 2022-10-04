package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class AccessService implements Serializable {

    String conformsTo;

    String endpointDescription;

    String endpointURL;

    String servesDataset;

    private String serviceSpecs;

}
