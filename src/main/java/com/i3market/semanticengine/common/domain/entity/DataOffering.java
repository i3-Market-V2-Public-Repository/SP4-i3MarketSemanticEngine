package com.i3market.semanticengine.common.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
//@ToString(onlyExplicitlyIncluded = true)
@Document(collection = "data_offering_t")
public class DataOffering implements Serializable {

    @Id
    String id;

    @TextIndexed(weight = 4)
    String provider;

    @TextIndexed(weight = 4)
    String marketId;

    @TextIndexed(weight = 4)
    String owner;
    @TextIndexed(weight = 4)
    String  providerDid;

   @TextIndexed(weight = 4)
    String  marketDid;
    @TextIndexed(weight = 4)
    String  ownerDid;

    boolean active;

    @TextIndexed(weight = 4)
    String ownerConsentForm;

    boolean inSharedNetwork;

    boolean personalData;

    @ToString.Include
    @TextIndexed(weight = 4)
    String dataOfferingTitle;

    @ToString.Include
    @TextIndexed(weight = 4)
    String dataOfferingDescription;

    @TextIndexed(weight = 4)
    @ToString.Include
    String category;

    @ToString.Include
    String status;

    @ToString.Include
    @Version
    Long version;

    @ToString.Include
    @CreatedDate
    Instant createdAt;

    @ToString.Include
    @LastModifiedDate
    Instant updatedAt;

    @ToString.Include
    @TextIndexed(weight = 4)
    String dataOfferingExpirationTime;

    ContractParameters contractParameters;

    PricingModel hasPricingModel;

    Dataset hasDataset;

}
