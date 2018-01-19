package com.deepfish.rest;

import com.deepfish.company.domain.CompanyMaturityLevel;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class RepositoryRestConfiguration extends RepositoryRestConfigurerAdapter {

  private final Validator validator;

  public RepositoryRestConfiguration(Validator validator) {
    this.validator = validator;
  }

  @Override
  public void configureRepositoryRestConfiguration(
      org.springframework.data.rest.core.config.RepositoryRestConfiguration config) {
    config.exposeIdsFor(CompanyMaturityLevel.class);
  }

  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
