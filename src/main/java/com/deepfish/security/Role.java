package com.deepfish.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
  ROLE_SUPER_ADMIN,
  ROLE_ADMIN,
  ROLE_EMPLOYER,
  ROLE_TALENT,
  ROLE_USER;

  private final GrantedAuthority grantedAuthority;

  Role() {
    this.grantedAuthority = new SimpleGrantedAuthority(this.name());
  }

  public GrantedAuthority toGrantedAuthority() {
    return grantedAuthority;
  }
}
