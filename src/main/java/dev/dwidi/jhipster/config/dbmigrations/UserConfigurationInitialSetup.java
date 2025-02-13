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

        // User Management Columns Configuration
        TemplateColumn userManagementConfig = new TemplateColumn();
        userManagementConfig.setTemplateName("user-management-columns");
        List<TemplateProperties> userManagementProperties = new ArrayList<>();
        userManagementProperties.add(new TemplateProperties(1, "First Name", "firstName", true, true));
        userManagementProperties.add(new TemplateProperties(2, "Last Name", "lastName", true, true));
        userManagementConfig.setProperties(userManagementProperties);
        configurations.add(userManagementConfig);

        // Role Columns Configuration
        TemplateColumn roleConfig = new TemplateColumn();
        roleConfig.setTemplateName("role-columns");
        List<TemplateProperties> roleProperties = new ArrayList<>();
        roleProperties.add(new TemplateProperties(1, "Role Name", "name", true, true));
        roleConfig.setProperties(roleProperties);
        configurations.add(roleConfig);

        config.setConfigurations(configurations);
        return config;
    }
}
