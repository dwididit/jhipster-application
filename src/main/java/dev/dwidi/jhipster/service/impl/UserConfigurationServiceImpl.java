package dev.dwidi.jhipster.service.impl;

import dev.dwidi.jhipster.domain.TemplateColumn;
import dev.dwidi.jhipster.domain.TemplateProperties;
import dev.dwidi.jhipster.domain.UserConfiguration;
import dev.dwidi.jhipster.repository.UserConfigurationRepository;
import dev.dwidi.jhipster.service.UserConfigurationService;
import dev.dwidi.jhipster.service.dto.TemplatePropertyRequestDTO;
import dev.dwidi.jhipster.service.dto.TemplateRequestDTO;
import dev.dwidi.jhipster.web.rest.errors.BadRequestAlertException;
import dev.dwidi.jhipster.web.rest.errors.ErrorConstants;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConfigurationServiceImpl implements UserConfigurationService {

    private static final String ENTITY_NAME = "userConfiguration";
    private final UserConfigurationRepository repository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<UserConfiguration> getUserConfiguration(String userId) {
        return Optional.ofNullable(
            repository
                .findByUserId(userId)
                .orElseThrow(() -> new BadRequestAlertException("User configuration not found", ENTITY_NAME, "idnotfound"))
        );
    }

    @Override
    public UserConfiguration addTemplateConfiguration(String userId, TemplateRequestDTO template) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BadRequestAlertException("User ID cannot be null or empty", ENTITY_NAME, "userinvalid");
        }

        if (template == null || template.getTemplateName() == null || template.getTemplateName().trim().isEmpty()) {
            throw new BadRequestAlertException("Template name cannot be null or empty", ENTITY_NAME, "templateinvalid");
        }

        UserConfiguration config = repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("User configuration not found for ID: " + userId, ENTITY_NAME, "usernotfound"));

        boolean templateExists = config.getConfigurations().stream().anyMatch(t -> t.getTemplateName().equals(template.getTemplateName()));

        if (templateExists) {
            throw new BadRequestAlertException(
                ErrorConstants.DEFAULT_TYPE,
                "Template with name '" + template.getTemplateName() + "' already exists",
                ENTITY_NAME,
                "templateexists"
            );
        }

        TemplateColumn newTemplate = new TemplateColumn();
        newTemplate.setTemplateName(template.getTemplateName());

        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().push("configurations", newTemplate);
        var result = mongoTemplate.updateFirst(query, update, UserConfiguration.class);

        if (result.getModifiedCount() == 0) {
            throw new BadRequestAlertException("Failed to update configuration", ENTITY_NAME, "updatefailed");
        }

        return repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("Updated configuration not found", ENTITY_NAME, "updatefailed"));
    }

    @Override
    public UserConfiguration addTemplateProperty(String userId, String templateName, TemplatePropertyRequestDTO propertyRequest) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BadRequestAlertException("User ID cannot be null or empty", ENTITY_NAME, "userinvalid");
        }

        if (templateName == null || templateName.trim().isEmpty()) {
            throw new BadRequestAlertException("Template name cannot be null or empty", ENTITY_NAME, "templateinvalid");
        }

        UserConfiguration config = repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("User configuration not found for ID: " + userId, ENTITY_NAME, "usernotfound"));

        TemplateColumn template = config
            .getConfigurations()
            .stream()
            .filter(t -> t.getTemplateName().equals(templateName))
            .findFirst()
            .orElseThrow(() -> new BadRequestAlertException("Template not found: " + templateName, ENTITY_NAME, "templatenotfound"));

        if (
            template.getProperties() != null &&
            template.getProperties().stream().anyMatch(p -> p.getName().equals(propertyRequest.getName()))
        ) {
            throw new BadRequestAlertException(
                "Property with name '" + propertyRequest.getName() + "' already exists",
                ENTITY_NAME,
                "propertyexists"
            );
        }

        TemplateProperties newProperty = new TemplateProperties();
        newProperty.setLabel(propertyRequest.getLabel());
        newProperty.setName(propertyRequest.getName());
        newProperty.setShow(propertyRequest.isShow());
        newProperty.setStrict(propertyRequest.isStrict());

        int nextIndex = 1;
        if (template.getProperties() != null && !template.getProperties().isEmpty()) {
            nextIndex = template.getProperties().stream().mapToInt(TemplateProperties::getIndex).max().getAsInt() + 1;
        }
        newProperty.setIndex(nextIndex);

        Query query = new Query(Criteria.where("userId").is(userId).and("configurations.templateName").is(templateName));
        Update update = new Update().push("configurations.$.properties", newProperty);

        var result = mongoTemplate.updateFirst(query, update, UserConfiguration.class);

        if (result.getModifiedCount() == 0) {
            throw new BadRequestAlertException("Failed to add property", ENTITY_NAME, "updatefailed");
        }

        return repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("Updated configuration not found", ENTITY_NAME, "updatefailed"));
    }

    @Override
    public UserConfiguration updateTemplateName(String userId, String oldTemplateName, String newTemplateName) {
        if (newTemplateName == null || newTemplateName.trim().isEmpty()) {
            throw new BadRequestAlertException("New template name cannot be null or empty", ENTITY_NAME, "templateinvalid");
        }

        if (!newTemplateName.matches("^[a-zA-Z0-9-_]+$")) {
            throw new BadRequestAlertException(
                "Template name can only contain letters, numbers, and hyphens",
                ENTITY_NAME,
                "templatenameinvalid"
            );
        }

        UserConfiguration config = repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("User configuration not found", ENTITY_NAME, "usernotfound"));

        boolean templateExists = config
            .getConfigurations()
            .stream()
            .anyMatch(template -> template.getTemplateName().equals(oldTemplateName));

        if (!templateExists) {
            throw new BadRequestAlertException("Template not found", ENTITY_NAME, "templatenotfound");
        }

        boolean newTemplateExists = config
            .getConfigurations()
            .stream()
            .anyMatch(template -> template.getTemplateName().equals(newTemplateName));

        if (newTemplateExists) {
            throw new BadRequestAlertException("Template with new name already exists", ENTITY_NAME, "templateexists");
        }

        Query query = new Query(Criteria.where("userId").is(userId).and("configurations.templateName").is(oldTemplateName));
        Update update = new Update().set("configurations.$.templateName", newTemplateName);

        var result = mongoTemplate.updateFirst(query, update, UserConfiguration.class);

        if (result.getModifiedCount() == 0) {
            throw new BadRequestAlertException("Failed to update template name", ENTITY_NAME, "updatefailed");
        }

        return repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("Updated configuration not found", ENTITY_NAME, "updatefailed"));
    }

    @Override
    public UserConfiguration updateTemplateProperties(String userId, String templateName, List<TemplateProperties> newProperties) {
        UserConfiguration config = repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("User configuration not found", ENTITY_NAME, "usernotfound"));

        boolean templateExists = config.getConfigurations().stream().anyMatch(template -> template.getTemplateName().equals(templateName));

        if (!templateExists) {
            throw new BadRequestAlertException("Template not found", ENTITY_NAME, "templatenotfound");
        }

        if (newProperties != null && !newProperties.isEmpty()) {
            validateIndexes(newProperties);
            validateDuplicates(newProperties);
        }

        Query query = new Query(Criteria.where("userId").is(userId).and("configurations.templateName").is(templateName));
        Update update = new Update().set("configurations.$.properties", newProperties);

        var result = mongoTemplate.updateFirst(query, update, UserConfiguration.class);

        if (result.getModifiedCount() == 0) {
            throw new BadRequestAlertException("Failed to update template properties", ENTITY_NAME, "updatefailed");
        }

        return repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("Updated configuration not found", ENTITY_NAME, "updatefailed"));
    }

    private void validateIndexes(List<TemplateProperties> properties) {
        List<TemplateProperties> sortedProps = properties
            .stream()
            .sorted(Comparator.comparingInt(TemplateProperties::getIndex))
            .collect(Collectors.toList());

        for (int i = 0; i < sortedProps.size(); i++) {
            int expectedIndex = i + 1;
            int actualIndex = sortedProps.get(i).getIndex();

            if (actualIndex != expectedIndex) {
                throw new BadRequestAlertException(
                    "Property indices must be incremental starting from 1. Found index " + actualIndex + " at position " + (i + 1),
                    ENTITY_NAME,
                    "invalidindex"
                );
            }
        }

        long uniqueIndices = properties.stream().map(TemplateProperties::getIndex).distinct().count();

        if (uniqueIndices != properties.size()) {
            throw new BadRequestAlertException("Properties cannot have duplicate indices", ENTITY_NAME, "duplicateindex");
        }
    }

    private void validateDuplicates(List<TemplateProperties> properties) {
        Map<String, Integer> labelCounts = new HashMap<>();
        Map<String, Integer> nameCounts = new HashMap<>();

        for (TemplateProperties prop : properties) {
            labelCounts.merge(prop.getLabel(), 1, Integer::sum);
            nameCounts.merge(prop.getName(), 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : labelCounts.entrySet()) {
            if (entry.getValue() > 1) {
                throw new BadRequestAlertException(
                    String.format("Found duplicate label: '%s' used %d times", entry.getKey(), entry.getValue()),
                    ENTITY_NAME,
                    "duplicatelabel"
                );
            }
        }

        for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
            if (entry.getValue() > 1) {
                throw new BadRequestAlertException(
                    String.format("Found duplicate name: '%s' used %d times", entry.getKey(), entry.getValue()),
                    ENTITY_NAME,
                    "duplicatename"
                );
            }
        }
    }

    @Override
    public void deleteTemplate(String userId, String templateName) {
        var config = repository
            .findByUserId(userId)
            .orElseThrow(() -> new BadRequestAlertException("User configuration not found", ENTITY_NAME, "usernotfound"));

        boolean templateExists = config.getConfigurations().stream().anyMatch(template -> template.getTemplateName().equals(templateName));

        if (!templateExists) {
            throw new BadRequestAlertException("Template not found", ENTITY_NAME, "templatenotfound");
        }

        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().pull("configurations", Query.query(Criteria.where("templateName").is(templateName)));

        var result = mongoTemplate.updateFirst(query, update, UserConfiguration.class);

        if (result.getModifiedCount() == 0) {
            throw new BadRequestAlertException("Failed to delete template", ENTITY_NAME, "deletefailed");
        }
    }
}
