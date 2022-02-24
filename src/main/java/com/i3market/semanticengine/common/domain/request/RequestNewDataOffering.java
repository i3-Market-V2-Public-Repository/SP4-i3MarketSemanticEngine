package com.i3market.semanticengine.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = RequestNewDataOffering.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestNewDataOffering {

    @NotNull(message = "provider is required")
    @NotBlank(message = "provider is required")
    @NotEmpty(message = "provider is required")
    @Schema(example = "required", required = true, type = "String")
    String provider;

    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "marketId is required")
    @NotBlank(message = "marketId is required")
    @NotEmpty(message = "marketId is required")
    String marketId;

    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "owner is required")
    @NotBlank(message = "owner is required")
    @NotEmpty(message = "owner is required")
    String owner;

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "dataOfferingTitle is required")
    @NotBlank(message = "dataOfferingTitle is required")
    @NotEmpty(message = "dataOfferingTitle is required")
    String dataOfferingTitle;

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "dataOfferingDescription is required")
    @NotBlank(message = "dataOfferingDescription is required")
    @NotEmpty(message = "dataOfferingDescription is required")
    String dataOfferingDescription;

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "category is required")
    @NotBlank(message = "category is required")
    @NotEmpty(message = "category is required")
    String category;

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "status is required")
    @NotBlank(message = "status is required")
    @NotEmpty(message = "status is required")
    String status;

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "dataOfferingExpirationTime is required")
    @NotBlank(message = "dataOfferingExpirationTime is required")
    @NotEmpty(message = "dataOfferingExpirationTime is required")
    String dataOfferingExpirationTime;

    RequestContractParameters contractParameters;

    RequestPricingModel hasPricingModel;

    RequestDataset hasDataset;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {
    }
}
