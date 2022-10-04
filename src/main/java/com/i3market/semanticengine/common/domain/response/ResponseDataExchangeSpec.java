package com.i3market.semanticengine.common.domain.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.EMPTY;



@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = ResponseDataExchangeSpec.Builder.class)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDataExchangeSpec {
     String encAlg;
     String signingAlg;
     String hashAlg;
     String ledgerContractAddress;
     String ledgerSignerAddress;
     int pooToPorDelay;
     int pooToPopDelay;
     int pooToSecretDelay;


    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {

    }
}
