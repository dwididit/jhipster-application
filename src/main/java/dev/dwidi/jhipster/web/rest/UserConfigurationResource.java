package dev.dwidi.jhipster.web.rest;

import dev.dwidi.jhipster.domain.TemplateProperties;
import dev.dwidi.jhipster.domain.UserConfiguration;
import dev.dwidi.jhipster.service.UserConfigurationService;
import dev.dwidi.jhipster.service.dto.TemplateNameUpdateDTO;
import dev.dwidi.jhipster.service.dto.TemplatePropertyRequestDTO;
import dev.dwidi.jhipster.service.dto.TemplateRequestDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-configurations")
@RequiredArgsConstructor
public class UserConfigurationResource {

    private final UserConfigurationService userConfigurationService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserConfiguration> getUserConfiguration(@PathVariable String userId) {
        return userConfigurationService.getUserConfiguration(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/templates")
    public ResponseEntity<UserConfiguration> addTemplateToConfiguration(
        @PathVariable String userId,
        @RequestBody TemplateRequestDTO template
    ) {
        UserConfiguration result = userConfigurationService.addTemplateConfiguration(userId, template);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{userId}/templates/{templateName}/properties")
    public ResponseEntity<UserConfiguration> addTemplateProperty(
        @PathVariable String userId,
        @PathVariable String templateName,
        @RequestBody TemplatePropertyRequestDTO property
    ) {
        UserConfiguration result = userConfigurationService.addTemplateProperty(userId, templateName, property);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{userId}/templates/{templateName}/name")
    public ResponseEntity<UserConfiguration> updateTemplateName(
        @PathVariable String userId,
        @PathVariable String templateName,
        @RequestBody TemplateNameUpdateDTO updateRequest
    ) {
        UserConfiguration result = userConfigurationService.updateTemplateName(userId, templateName, updateRequest.getNewTemplateName());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{userId}/templates/{templateName}/properties")
    public ResponseEntity<UserConfiguration> updateTemplateProperties(
        @PathVariable String userId,
        @PathVariable String templateName,
        @RequestBody List<TemplateProperties> properties
    ) {
        UserConfiguration results = userConfigurationService.updateTemplateProperties(userId, templateName, properties);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{userId}/templates/{templateName}")
    public ResponseEntity<Void> removeTemplate(@PathVariable String userId, @PathVariable String templateName) {
        userConfigurationService.deleteTemplate(userId, templateName);
        return ResponseEntity.ok().build();
    }
}
