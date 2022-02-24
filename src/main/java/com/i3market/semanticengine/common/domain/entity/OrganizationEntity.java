package com.i3market.semanticengine.common.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationEntity implements Serializable {

    @NonNull
    String organizationId;

    @NonNull
    String name;

    @Builder.Default
    String description = null;

    @Builder.Default
    String address = null;

    @Builder.Default
    String contactPoint = null;

}
