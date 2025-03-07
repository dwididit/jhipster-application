package dev.dwidi.jhipster.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.dwidi.jhipster.config.Constants;
import dev.dwidi.jhipster.domain.Authority;
import dev.dwidi.jhipster.domain.User;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class AdminUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 50)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 50)
    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Email
    @Size(min = 5, max = 254)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 256)
    private String imageUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean activated = false;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 2, max = 10)
    private String langKey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant createdDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastModifiedBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant lastModifiedDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> authorities;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminUserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
    }
}
