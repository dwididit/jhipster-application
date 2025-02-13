package dev.dwidi.jhipster.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateColumn {

    @Field("template_name")
    private String templateName;

    @Field("properties")
    private List<TemplateProperties> properties = new ArrayList<>();
}
