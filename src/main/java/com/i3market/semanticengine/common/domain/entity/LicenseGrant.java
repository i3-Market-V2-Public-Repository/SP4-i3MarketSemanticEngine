package com.i3market.semanticengine.common.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class LicenseGrant implements Serializable {


    boolean transferable;

    boolean exclusiveness;

    boolean paidUp;

    boolean revocable;

    boolean  processing;

    boolean  modifying;

    boolean analyzing;

    boolean storingData;

    boolean storingCopy;

    boolean  reproducing;

    boolean distributing;

    boolean  loaning;

    boolean selling;

    boolean  renting;

    boolean furtherLicensing;

    boolean leasing;

}
