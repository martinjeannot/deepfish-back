package com.deepfish.security.auth;

import com.deepfish.security.AuthorizationServerConfiguration;
import java.util.Arrays;
import java.util.HashSet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenForge {


  private final DefaultTokenServices defaultTokenServices;

  private final TokenEnhancer tokenEnhancer;

  public JwtTokenForge(
      DefaultTokenServices defaultTokenServices,
      TokenEnhancer tokenEnhancer) {
    this.defaultTokenServices = defaultTokenServices;
    this.tokenEnhancer = tokenEnhancer;
  }

  public OAuth2AccessToken forgeToken(UserDetails userDetails) {
    OAuth2Request request = new OAuth2Request(null, "67e43464e9c0483faaf7b773018b2b60", null, true,
        new HashSet<>(Arrays.asList(AuthorizationServerConfiguration.AUTHORIZATION_SCOPE)), null,
        null, null, null);
    OAuth2Authentication authentication = new OAuth2Authentication(request,
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    authentication.setAuthenticated(true);
    OAuth2AccessToken jwtAccessToken = defaultTokenServices.createAccessToken(authentication);
    return tokenEnhancer.enhance(jwtAccessToken, authentication);
  }
}
