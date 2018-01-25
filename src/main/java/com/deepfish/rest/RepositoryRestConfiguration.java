package com.deepfish.rest;

import com.deepfish.company.domain.CompanyMaturityLevel;
import com.deepfish.talent.domain.CommodityType;
import com.deepfish.talent.domain.FixedLocation;
import com.deepfish.talent.domain.Job;
import com.deepfish.talent.domain.TaskType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    config.exposeIdsFor(CompanyMaturityLevel.class, Job.class, CommodityType.class, TaskType.class,
        FixedLocation.class);
  }

  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
