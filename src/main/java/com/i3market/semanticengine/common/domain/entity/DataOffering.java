package com.i3market.semanticengine.common.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Document(collection = "data_offering_t")
public class DataOffering implements Serializable {

    @Id
    String id;

    String provider;

    String marketId;

    String owner;

    @ToString.Include
    String dataOfferingTitle;

    @ToString.Include
    String dataOfferingDescription;

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
    String dataOfferingExpirationTime;

    ContractParameters contractParameters;

    PricingModel hasPricingModel;

    Dataset hasDataset;

}
