package com.deepfish.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;

  public WebSecurityConfiguration(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()).and()
        .inMemoryAuthentication().withUser("admin@deepfish.fr").password("password")
        .authorities("ROLE_SUPER_ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .formLogin().disable()
        .csrf().disable() // FIXME
        .authorizeRequests()
        .antMatchers("/auth/linkedin/callback").permitAll()
        .antMatchers("/employers/sign-up").permitAll()
        .antMatchers("/employers/password-reset").permitAll()
        .antMatchers("/employers/typeform").permitAll()
        .antMatchers("/requirements/typeform").permitAll()
        .antMatchers("/calendly/webhooks/*").permitAll()
        .anyRequest().denyAll();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.applyPermitDefaultValues();
    corsConfiguration.addAllowedMethod("*"); // this should only be applied to REST endpoints
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
