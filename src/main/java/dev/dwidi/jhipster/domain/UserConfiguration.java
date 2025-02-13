package dev.dwidi.jhipster.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "user_configuration")
public class UserConfiguration extends AbstractAuditingEntity<String> {

    @Id
    private String id;

    @NotNull
    @Field("user_id")
    @Indexed(unique = true)
    private String userId;

    @DBRef(lazy = true)
    @Field("user")
    @JsonBackReference
    private User user;

    @Field("configurations")
    private List<TemplateColumn> configurations = new ArrayList<>();

    public TemplateColumn getTemplateColumnByName(String templateName) {
        if (configurations != null) {
            return configurations.stream().filter(template -> template.getTemplateName().equals(templateName)).findFirst().orElse(null);
        }
        return null;
    }

    public void setUser(User user) {
        User oldUser = this.user;
        this.user = user;
        this.userId = user != null ? user.getId() : null;

        if (user != null && !this.equals(user.getUserConfiguration())) {
            user.setUserConfiguration(this);
        }
        if (oldUser != null && oldUser != user) {
            oldUser.setUserConfiguration(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserConfiguration that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
