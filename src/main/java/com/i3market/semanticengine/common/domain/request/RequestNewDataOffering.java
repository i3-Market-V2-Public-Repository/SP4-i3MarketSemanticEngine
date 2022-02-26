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
    @lombok.Builder.Default
    String provider = "required";

    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "marketId is required")
    @NotBlank(message = "marketId is required")
    @NotEmpty(message = "marketId is required")
    @lombok.Builder.Default
    String marketId = "required";

    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "owner is required")
    @NotBlank(message = "owner is required")
    @NotEmpty(message = "owner is required")
    @lombok.Builder.Default
    String owner = "required";

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "dataOfferingTitle is required")
    @NotBlank(message = "dataOfferingTitle is required")
    @NotEmpty(message = "dataOfferingTitle is required")
    @lombok.Builder.Default
    String dataOfferingTitle = "required";

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "dataOfferingDescription is required")
    @NotBlank(message = "dataOfferingDescription is required")
    @NotEmpty(message = "dataOfferingDescription is required")
    @lombok.Builder.Default
    String dataOfferingDescription = "required";

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "category is required")
    @NotBlank(message = "category is required")
    @NotEmpty(message = "category is required")
    @lombok.Builder.Default
    String category = "required";

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "status is required")
    @NotBlank(message = "status is required")
    @NotEmpty(message = "status is required")
    @lombok.Builder.Default
    String status = "required";

    @ToString.Include
    @Schema(example = "required", required = true, type = "String")
    @NotNull(message = "dataOfferingExpirationTime is required")
    @NotBlank(message = "dataOfferingExpirationTime is required")
    @NotEmpty(message = "dataOfferingExpirationTime is required")
    @lombok.Builder.Default
    String dataOfferingExpirationTime = "required";

    @ToString.Include
    RequestContractParameters contractParameters;

    @ToString.Include
    RequestPricingModel hasPricingModel;

    @ToString.Include
    RequestDataset hasDataset;

    @JsonPOJOBuilder(withPrefix = EMPTY)
    public static class Builder {
    }
}
