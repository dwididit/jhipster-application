package dev.dwidi.jhipster.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Template name update request")
public class TemplateNameUpdateDTO {

    @Schema(description = "New name for the template", required = true)
    private String newTemplateName;
}
