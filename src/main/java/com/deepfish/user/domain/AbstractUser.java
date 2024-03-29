package com.deepfish.user.domain;

import com.deepfish.user.converters.AuthoritiesConverter;
import com.deepfish.user.serialization.PasswordDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@MappedSuperclass
@Data
@Accessors(chain = true)
public abstract class AbstractUser implements UserDetails, Identifiable<UUID> {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @JsonProperty(access = Access.WRITE_ONLY)
  @JsonDeserialize(using = PasswordDeserializer.class)
  @NotBlank
  private String password;

  @NotBlank
  private String username;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String phoneNumber;

  @Convert(converter = AuthoritiesConverter.class)
  @NotEmpty
  private Collection<? extends GrantedAuthority> authorities;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now(Clock.systemUTC());

  @NotNull
  private LocalDateTime lastSignedInAt = LocalDateTime.now(Clock.systemUTC());

  /**
   * Enable user authentication by setting all of Spring's authentication-blocking properties to
   * true. Usually called upon user creation.
   */
  public void enableAuthentication() {
    accountNonExpired = true;
    accountNonLocked = true;
    credentialsNonExpired = true;
    enabled = true;
  }
}
