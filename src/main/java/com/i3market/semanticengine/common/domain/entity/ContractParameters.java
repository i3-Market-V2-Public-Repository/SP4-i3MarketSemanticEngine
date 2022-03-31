package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class ContractParameters implements Serializable {

    String interestOfProvider;

    String interestDescription;

    String hasGoverningJurisdiction;

    String purpose;

    String purposeDescription;

    IntendedUse hasIntendedUse;

    LicenseGrant hasLicenseGrant;

}
