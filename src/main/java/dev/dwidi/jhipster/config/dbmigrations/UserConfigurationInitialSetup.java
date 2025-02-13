package dev.dwidi.jhipster.config.dbmigrations;

import dev.dwidi.jhipster.domain.TemplateColumn;
import dev.dwidi.jhipster.domain.TemplateProperties;
import dev.dwidi.jhipster.domain.User;
import dev.dwidi.jhipster.domain.UserConfiguration;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "user-configuration-initialization", order = "002")
public class UserConfigurationInitialSetup {

    private final MongoTemplate template;

    public UserConfigurationInitialSetup(MongoTemplate template) {
        this.template = template;
    }

    @Execution
    public void changeSet() {
        List<User> users = template.findAll(User.class);
        for (User user : users) {
            UserConfiguration userConfig = createUserConfiguration(user);
            template.save(userConfig);
        }
    }

    @RollbackExecution
    public void rollback() {}

    private UserConfiguration createUserConfiguration(User user) {
        UserConfiguration config = new UserConfiguration();
        config.setUserId(user.getId());
        config.setUser(user);

        List<TemplateColumn> configurations = new ArrayList<>();

        TemplateColumn userManagementConfig = new TemplateColumn();
        userManagementConfig.setTemplateName("user-management-columns");

        List<TemplateProperties> userManagementProperties = new ArrayList<>();
        userManagementProperties.add(new TemplateProperties(1, "ID", "id", true, true));
        userManagementProperties.add(new TemplateProperties(2, "Login", "login", true, true));
        userManagementProperties.add(new TemplateProperties(3, "Email", "email", true, true));
        userManagementProperties.add(new TemplateProperties(4, "Activated", "activated", true, true));
        userManagementProperties.add(new TemplateProperties(5, "Language", "langKey", true, true));
        userManagementProperties.add(new TemplateProperties(6, "Profiles", "authorities", true, true));
        userManagementProperties.add(new TemplateProperties(7, "Created date", "createdDate", true, true));
        userManagementProperties.add(new TemplateProperties(8, "Modified by", "lastModifiedBy", true, true));
        userManagementProperties.add(new TemplateProperties(9, "Modified date", "lastModifiedDate", true, true));

        userManagementConfig.setProperties(userManagementProperties);
        configurations.add(userManagementConfig);

        config.setConfigurations(configurations);
        return config;
    }
}
