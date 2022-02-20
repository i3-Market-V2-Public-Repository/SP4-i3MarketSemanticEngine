package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class Distribution implements Serializable {

    String title;

    String description;

    String license;

    String accessRights;

    String downloadType;

    String conformsTo;

    String mediaType;

    String packageFormat;

    AccessService accessService;

}
