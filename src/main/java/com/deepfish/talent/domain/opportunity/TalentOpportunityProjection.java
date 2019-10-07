package com.deepfish.talent.domain.opportunity;

import com.deepfish.company.domain.DefaultCompanyProjection;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.talent.domain.conditions.JobType;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "talent", types = {Opportunity.class})
public interface TalentOpportunityProjection {

  UUID getId();

  Requirement getRequirement();

  @Value("#{target.getRequirement().getCompany()}")
  DefaultCompanyProjection getCompany();

  @Value("#{target.getRequirement().getJobType()}")
  JobType getJobType();

  @Value("#{target.getRequirement().getLocation()}")
  String getLocation();

  String getPitch();

  OpportunityStatus getTalentStatus();

  LocalDateTime getSeenByTalentAt();

  OpportunityStatus getEmployerStatus();
}
