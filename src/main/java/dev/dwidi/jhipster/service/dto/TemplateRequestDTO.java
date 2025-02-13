package dev.dwidi.jhipster.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Template creation request")
public class TemplateRequestDTO {

    @Schema(description = "Name of the template", required = true)
    private String templateName;
}
