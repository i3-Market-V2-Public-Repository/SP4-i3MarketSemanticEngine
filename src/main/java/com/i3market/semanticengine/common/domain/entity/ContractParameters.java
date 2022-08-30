package com.i3market.semanticengine.common.domain.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class ContractParameters implements Serializable {
    @TextIndexed(weight = 4)
    String interestOfProvider;
    @TextIndexed(weight = 4)
    String interestDescription;
    @TextIndexed(weight = 4)
    String hasGoverningJurisdiction;
    @TextIndexed(weight = 4)
    String purpose;
    @TextIndexed(weight = 4)
    String purposeDescription;

    IntendedUse hasIntendedUse;

    LicenseGrant hasLicenseGrant;

}
