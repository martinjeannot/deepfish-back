package com.deepfish.rest;

import com.deepfish.company.domain.Company;
import com.deepfish.company.domain.CompanyMaturityLevel;
import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.Requirement;
import com.deepfish.employer.domain.Seniority;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.conditions.CommodityType;
import com.deepfish.talent.domain.conditions.FixedLocation;
import com.deepfish.talent.domain.conditions.Job;
import com.deepfish.talent.domain.conditions.TaskType;
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
    config.exposeIdsFor(Talent.class, Employer.class, Company.class, CompanyMaturityLevel.class,
        Job.class, CommodityType.class, TaskType.class, FixedLocation.class, Seniority.class,
        Requirement.class);
  }

  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
