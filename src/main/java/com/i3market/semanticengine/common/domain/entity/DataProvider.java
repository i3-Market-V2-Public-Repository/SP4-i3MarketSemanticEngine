package com.i3market.semanticengine.common.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Document(collection = "data_provider_t")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataProvider implements Serializable {


    @Id
    String id;

    @Indexed(unique = true)
    @NonNull
    String providerId;

    @NonNull
    String name;

    @Builder.Default
    String description = null;

    Instant createdAt;

    Instant updatedAt;

    List<OrganizationEntity> organization = new ArrayList<>();

}
