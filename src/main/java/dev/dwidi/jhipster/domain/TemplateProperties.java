package dev.dwidi.jhipster.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateProperties {

    @Field("index")
    private Integer index;

    @Field("label")
    private String label;

    @Field("name")
    private String name;

    @Field("show")
    private Boolean show;

    @Field("strict")
    private Boolean strict;
}
