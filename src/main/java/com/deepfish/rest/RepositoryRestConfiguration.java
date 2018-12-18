package com.deepfish.rest;

import com.deepfish.company.domain.Company;
import com.deepfish.company.domain.CompanyMaturityLevel;
import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.employer.domain.requirement.Seniority;
import com.deepfish.interview.domain.Interview;
import com.deepfish.talent.domain.QueryableTalent;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.conditions.CommodityType;
import com.deepfish.talent.domain.conditions.FixedLocation;
import com.deepfish.talent.domain.conditions.IndustryType;
import com.deepfish.talent.domain.conditions.JobType;
import com.deepfish.talent.domain.conditions.TaskType;
import com.deepfish.talent.domain.opportunity.Opportunity;
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
    config.exposeIdsFor(Talent.class, QueryableTalent.class, Opportunity.class, Employer.class,
        Company.class, CompanyMaturityLevel.class, JobType.class, CommodityType.class,
        TaskType.class, IndustryType.class, FixedLocation.class, Seniority.class,
        Requirement.class, Interview.class);
  }

  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
