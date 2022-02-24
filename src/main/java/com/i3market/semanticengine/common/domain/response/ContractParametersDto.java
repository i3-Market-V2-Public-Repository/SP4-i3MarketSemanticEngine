package com.i3market.semanticengine.common.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = ContractParametersDto.Builder.class)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractParametersDto {

    @lombok.Builder.Default
    @JsonIgnore
    String type = "http://i3-market.eu/backplane/core/ContractParameters";

    String interestOfProvider;

    String interestDescription;

    String hasGoverningJurisdiction;

    String purpose;

    String purposeDescription;

    IntendedUseDto hasIntendedUse;

    LicenseGrantDto hasLicenseGrant;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }
}
