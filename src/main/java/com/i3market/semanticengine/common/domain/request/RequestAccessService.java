package com.i3market.semanticengine.common.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestAccessService {

    String conformsTo;

    @NotNull(message = "purposeDescription is required")
    @NotBlank(message = "purposeDescription is required")
    @NotEmpty(message = "purposeDescription is required")
    @Schema(example = "required", required = true, type = "String")
    @lombok.Builder.Default
    String endpointDescription = "require";

    @Schema(example = "https://example.com/download/testdata.json", required = true, type = "String")
    @lombok.Builder.Default
    String endpointURL = "require";

    @lombok.Builder.Default
    String servesDataset = "require";

    @lombok.Builder.Default
    private String serviceSpecs = "require";
}
