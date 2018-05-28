package com.deepfish.security;

import java.util.Arrays;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class SystemAuthentication {

  private static final Authentication authentication;

  static {
    authentication = new PreAuthenticatedAuthenticationToken("system", null,
        Arrays.asList(Role.ROLE_SUPER_ADMIN.toGrantedAuthority(),
            Role.ROLE_ADMIN.toGrantedAuthority()));
  }

  private SystemAuthentication() {
  }

  public static Authentication getAuthentication() {
    return authentication;
  }
}
