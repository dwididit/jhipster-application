package dev.dwidi.jhipster.service;

import dev.dwidi.jhipster.domain.TemplateProperties;
import dev.dwidi.jhipster.domain.UserConfiguration;
import dev.dwidi.jhipster.service.dto.TemplatePropertyRequestDTO;
import dev.dwidi.jhipster.service.dto.TemplateRequestDTO;
import java.util.List;
import java.util.Optional;

public interface UserConfigurationService {
    Optional<UserConfiguration> getUserConfiguration(String userId);

    UserConfiguration addTemplateConfiguration(String userId, TemplateRequestDTO templateRequest);

    UserConfiguration addTemplateProperty(String userId, String templateName, TemplatePropertyRequestDTO propertyRequest);

    UserConfiguration updateTemplateName(String userId, String oldTemplateName, String newTemplateName);

    UserConfiguration updateTemplateProperties(String userId, String templateName, List<TemplateProperties> newProperties);

    void deleteTemplate(String userId, String templateName);
}
