package dev.dwidi.jhipster.service;

import dev.dwidi.jhipster.domain.TemplateProperties;
import dev.dwidi.jhipster.domain.User;
import dev.dwidi.jhipster.repository.UserConfigurationRepository;
import dev.dwidi.jhipster.repository.UserRepository;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RsqlColumnService {

    private final UserConfigurationRepository userConfigurationRepository;
    private final UserRepository userRepository;

    private List<TemplateProperties> getColumnProperties(String templateName) {
        log.debug("Getting column properties for templateName: {}", templateName);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findOneByLogin(username).orElseThrow(() -> new RuntimeException("Current user not found"));

        var configOpt = userConfigurationRepository.findByUserId(currentUser.getId());
        log.debug("Found configuration: {}", configOpt.orElse(null));

        if (configOpt.isEmpty()) {
            log.debug("No configuration found for user: {}", username);
            return Collections.emptyList();
        }

        var config = configOpt.get();
        var template = config.getConfigurations().stream().filter(t -> templateName.equals(t.getTemplateName())).findFirst();

        log.debug("Found template: {}", template.orElse(null));

        if (template.isEmpty()) {
            log.debug("No template found for name: {}", templateName);
            return Collections.emptyList();
        }

        var properties = template.get().getProperties();
        log.debug("Found properties: {}", properties);

        return properties;
    }

    public List<String> getVisibleColumns(String userId, String templateName) {
        log.debug("Getting visible columns for userId: {} and templateName: {}", userId, templateName);

        List<TemplateProperties> properties = getColumnProperties(templateName);
        if (properties.isEmpty()) {
            log.debug("No properties found, returning empty list");
            return Collections.emptyList();
        }

        List<String> visibleColumns = properties
            .stream()
            .filter(prop -> Boolean.TRUE.equals(prop.getShow()))
            .map(TemplateProperties::getName)
            .collect(Collectors.toList());

        log.debug("Found visible columns: {}", visibleColumns);
        return visibleColumns;
    }

    public String enhanceQueryWithVisibleColumns(String userId, String templateName, String existingQuery) {
        log.debug("Enhancing query. Existing query: {}", existingQuery);

        List<TemplateProperties> properties = getColumnProperties(templateName);
        List<String> hiddenColumns = properties
            .stream()
            .filter(prop -> Boolean.FALSE.equals(prop.getShow()))
            .map(TemplateProperties::getName)
            .collect(Collectors.toList());

        log.debug("Hidden columns: {}", hiddenColumns);

        if (hiddenColumns.isEmpty()) {
            log.debug("No hidden columns found, returning original query");
            return existingQuery;
        }

        StringBuilder queryBuilder = new StringBuilder();
        if (existingQuery != null && !existingQuery.trim().isEmpty()) {
            queryBuilder.append(existingQuery).append(";");
        }

        String hideQuery = hiddenColumns.stream().map(column -> column + "=null").collect(Collectors.joining(";"));
        queryBuilder.append(hideQuery);

        String enhancedQuery = queryBuilder.toString();
        log.debug("Enhanced query: {}", enhancedQuery);
        return enhancedQuery;
    }

    public <T> T filterDtoFields(T dto, List<String> visibleColumns) {
        if (dto == null) {
            return null;
        }

        log.debug("Filtering DTO: {} with visible columns: {}", dto, visibleColumns);

        if (visibleColumns == null || visibleColumns.isEmpty()) {
            log.debug("No visible columns specified, returning original DTO");
            return dto;
        }

        Class<?> dtoClass = dto.getClass();
        List<Field> allFields = getAllFields(dtoClass);

        for (Field field : allFields) {
            String fieldName = field.getName();
            if (!visibleColumns.contains(fieldName) && !fieldName.equals("serialVersionUID")) {
                try {
                    field.setAccessible(true);
                    if (!field.getType().isPrimitive()) {
                        Object oldValue = field.get(dto);
                        field.set(dto, null);
                        log.debug("Set field {} from {} to null", fieldName, oldValue);
                    }
                } catch (Exception e) {
                    log.debug("Could not set field {} to null: {}", fieldName, e.getMessage());
                }
            }
        }

        return dto;
    }

    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = type;
        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return fields;
    }
}
