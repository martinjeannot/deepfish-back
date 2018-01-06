package com.deepfish.rest;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

//@Component
public class CORSConfiguration extends RepositoryRestConfigurerAdapter {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    super.configureRepositoryRestConfiguration(config);
    config
        .getCorsRegistry()
        .addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*").allowCredentials(false);
  }
}
