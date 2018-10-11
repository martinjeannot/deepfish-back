package com.deepfish.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface TokenFactory {

  OAuth2AccessToken createToken(UserDetails userDetails);
}
