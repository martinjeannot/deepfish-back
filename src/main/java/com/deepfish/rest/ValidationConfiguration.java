package com.deepfish.rest;

import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class ValidationConfiguration extends RepositoryRestConfigurerAdapter {

  private final Validator validator;

  public ValidationConfiguration(Validator validator) {
    this.validator = validator;
  }

  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
