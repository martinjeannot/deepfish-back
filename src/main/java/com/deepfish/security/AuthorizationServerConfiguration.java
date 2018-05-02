package com.deepfish.security;


import com.deepfish.user.domain.AbstractUser;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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

  private final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12; // 12 hours
  private final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30; // 30 days

  public static final String[] AUTHORIZATION_SCOPE = new String[]{"read", "write"};

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  private final AuthenticationManager authenticationManager;

  private final UserDetailsService userDetailsService;

  private final CorsConfigurationSource corsConfigurationSource;

  public AuthorizationServerConfiguration(
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService,
      CorsConfigurationSource corsConfigurationSource) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.corsConfigurationSource = corsConfigurationSource;
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenStore(tokenStore());
    tokenServices.setSupportRefreshToken(true);
    tokenServices.setReuseRefreshToken(false);
    tokenServices.setTokenEnhancer(tokenEnhancer());
    return tokenServices;
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(tokenEnhancer());
  }

  @Bean
  public JwtAccessTokenConverter tokenEnhancer() {
    JwtAccessTokenConverter tokenEnhancer = new JwtAccessTokenConverter() {
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
    tokenEnhancer.setSigningKey("DjK8G7PX3zcYeG4z");
    return tokenEnhancer;
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
        // fixing a bug where the user details service is not retrieved from the authentication manager
        // Error message : "Handling error: IllegalStateException, UserDetailsService is required."
        .userDetailsService(userDetailsService)
        // TODO: reuse our tokenServices, see AuthorizationServerEndpointsConfigurer.createDefaultTokenServices
        //.tokenServices(tokenServices())
        .tokenStore(tokenStore())
        .reuseRefreshTokens(false)
        //.accessTokenConverter(tokenEnhancer())
        .tokenEnhancer(tokenEnhancer());
  }
}