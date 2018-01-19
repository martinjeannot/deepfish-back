package com.deepfish.user.domain;

import com.deepfish.user.converters.AuthoritiesConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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

  @JsonIgnore
  @NotBlank
  private String password;

  @NotBlank
  @Column(unique = true)
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
