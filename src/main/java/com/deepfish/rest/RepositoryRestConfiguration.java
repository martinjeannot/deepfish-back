package com.deepfish.rest;

import com.deepfish.company.domain.Company;
import com.deepfish.company.domain.CompanyMaturityLevel;
import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.employer.domain.requirement.Seniority;
import com.deepfish.interview.domain.Interview;
import com.deepfish.talent.domain.QueryableTalent;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.Utm;
import com.deepfish.talent.domain.conditions.ClientIndustryType;
import com.deepfish.talent.domain.conditions.CommodityType;
import com.deepfish.talent.domain.conditions.FixedLocation;
import com.deepfish.talent.domain.conditions.IndustryType;
import com.deepfish.talent.domain.conditions.JobType;
import com.deepfish.talent.domain.conditions.TaskType;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityDatum;
import com.deepfish.user.domain.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    config.exposeIdsFor(
        Talent.class, QueryableTalent.class, Opportunity.class, Employer.class, Company.class,
        CompanyMaturityLevel.class, JobType.class, CommodityType.class, TaskType.class,
        IndustryType.class, ClientIndustryType.class, FixedLocation.class, Seniority.class,
        Requirement.class, OpportunityDatum.class, Interview.class, Utm.class, User.class
    );
  }

  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }

  @Override
  public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
    super.configureJacksonObjectMapper(objectMapper);
    // tells Jackson to preserve time zones upon deserialization
    objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
  }
}
