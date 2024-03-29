package com.deepfish.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  private final DefaultTokenServices defaultTokenServices;

  private final TokenStore tokenStore;

  public ResourceServerConfiguration(DefaultTokenServices defaultTokenServices,
      TokenStore tokenStore) {
    this.defaultTokenServices = defaultTokenServices;
    this.tokenStore = tokenStore;
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources
        .resourceId(resourceId)
        .tokenServices(defaultTokenServices)
        .tokenStore(tokenStore);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .requestMatcher(new OAuthRequestedMatcher())
        .cors().and()
        .csrf().disable()
        .anonymous().disable()
        .authorizeRequests().anyRequest().authenticated();
  }

  private static class OAuthRequestedMatcher implements RequestMatcher {

    public boolean matches(HttpServletRequest request) {
      String auth = request.getHeader("Authorization");
      // Determine if the client request contained an OAuth Authorization
      boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
      boolean haveAccessToken = request.getParameter("access_token") != null;
      return haveOauth2Token || haveAccessToken;
    }
  }
}
