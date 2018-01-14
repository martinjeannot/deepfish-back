package com.deepfish.rest;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * Similar to the Spring Web MVC's CORS configuration that can be currently found in {@link
 * com.deepfish.security.WebSecurityConfiguration}, this component takes care of enabling CORS for
 * Spring Data Rest. The SDR reference documentation states : "Existing Spring Web MVC CORS
 * configuration is not applied to Spring Data REST."
 */
//@Component
public class CORSConfiguration extends RepositoryRestConfigurerAdapter {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config
        .getCorsRegistry()
        .addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*")
        .allowCredentials(false);
  }
}
