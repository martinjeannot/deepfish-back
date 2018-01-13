package com.deepfish.security;


import com.deepfish.user.domain.AbstractUser;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  private final int ACCESS_TOKEN_VALIDITY_SECONDS = 10000;
  private final int REFRESH_TOKEN_VALIDITY_SECONDS = 30000;

  public static final String[] AUTHORIZATION_SCOPE = new String[]{"read", "write"};

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  private final AuthenticationManager authenticationManager;

  private final CorsConfigurationSource corsConfigurationSource;

  public AuthorizationServerConfiguration(
      AuthenticationManager authenticationManager,
      CorsConfigurationSource corsConfigurationSource) {
    this.authenticationManager = authenticationManager;
    this.corsConfigurationSource = corsConfigurationSource;
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
      @Override
      public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
          OAuth2Authentication authentication) {
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation
            .put("user_id", ((AbstractUser) authentication.getPrincipal()).getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return super.enhance(accessToken, authentication);
      }
    };
    accessTokenConverter.setSigningKey("123");
    return accessTokenConverter;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        // allowing access to the token only for clients with 'ROLE_TRUSTED_CLIENT' authority
        .tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
        .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
        // allowing preflight requests on oauth endpoints
        .addTokenEndpointAuthenticationFilter(new CorsFilter(corsConfigurationSource));
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    // TODO : move our client details to config file using Spring Boot 2 Propert Mappings
    // https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html#jc-oauth2login-boot-property-mappings
    clients
        .inMemory()
        .withClient("67e43464e9c0483faaf7b773018b2b60")
        .secret("9c7d7778e0534031aa0ed684bba16546")
        .authorizedGrantTypes("client_credentials", "password", "refresh_token")
        .authorities("ROLE_TRUSTED_CLIENT")
        .scopes(AUTHORIZATION_SCOPE)
        .resourceIds(resourceId)
        .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
        .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .authenticationManager(authenticationManager)
        //.tokenServices(tokenServices())
        .tokenStore(tokenStore())
        .accessTokenConverter(accessTokenConverter());
  }
}