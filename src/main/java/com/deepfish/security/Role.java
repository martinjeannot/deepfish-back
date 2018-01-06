package com.deepfish.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
  ROLE_USER,
  ROLE_ADMIN;

  public GrantedAuthority toGrantedAuthority() {
    return new SimpleGrantedAuthority(name());
  }
}
