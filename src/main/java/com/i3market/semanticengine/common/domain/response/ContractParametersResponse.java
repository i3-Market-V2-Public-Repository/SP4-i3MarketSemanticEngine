package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = ContractParametersResponse.Builder.class)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractParametersResponse {
    
    String offeringId;

    Long version;

    String provider;

    String providerDid;

    boolean active;

    boolean dataStream;

    boolean personalData;

    @ToString.Include
    String category;

    String dataOfferingTitle;

    ContractParametersDto contractParameters;

    PricingModelDto hasPricingModel;

    ResponseDataExchangeSpec dataExchangeSpec;

}
