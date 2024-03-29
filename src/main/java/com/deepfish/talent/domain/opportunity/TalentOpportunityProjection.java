package com.deepfish.talent.domain.opportunity;

import com.deepfish.company.domain.DefaultCompanyProjection;
import com.deepfish.employer.domain.TalentEmployerProjection;
import com.deepfish.employer.domain.requirement.Requirement;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "talent", types = {Opportunity.class})
public interface TalentOpportunityProjection {

  UUID getId();

  TalentEmployerProjection getEmployer();

  Requirement getRequirement();

  @Value("#{target.getRequirement().getCompany()}")
  DefaultCompanyProjection getCompany();

  String getName();

  Float getBaseSalaryFrom();

  Float getBaseSalaryTo();

  String getPitch();

  OpportunityStatus getTalentStatus();

  LocalDateTime getSeenByTalentAt();

  OpportunityStatus getEmployerStatus();

  String getEmployerDeclinationReason();
}
