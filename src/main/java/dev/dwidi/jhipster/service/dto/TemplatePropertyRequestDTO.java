package dev.dwidi.jhipster.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Template property creation request")
public class TemplatePropertyRequestDTO {

    @Schema(description = "Display label for the property", required = true)
    private String label;

    @Schema(description = "Property name/identifier", required = true)
    private String name;

    @Schema(description = "Visibility flag", defaultValue = "true")
    private boolean show = true;

    @Schema(description = "Strict validation flag", defaultValue = "true")
    private boolean strict = true;
}
