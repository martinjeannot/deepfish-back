package com.deepfish.user.domain;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "Users")
@Data
public class User implements UserDetails, Identifiable<UUID> {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotBlank
  private String password;

  @NotBlank
  @Column(unique = true)
  private String username;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Transient
  private Set<GrantedAuthority> authorities;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;
}
