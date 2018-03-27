package com.deepfish.talent.domain.opportunity;

import com.deepfish.company.domain.Company;
import com.deepfish.employer.domain.Requirement;
import com.deepfish.talent.domain.conditions.Job;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "talent", types = {Opportunity.class})
public interface TalentOpportunityProjection {

  UUID getId();

  OpportunityStatus getStatus();

  Requirement getRequirement();

  String getPitch();

  @Value("#{target.getRequirement().getCompany()}")
  Company getCompany();

  @Value("#{target.getRequirement().getJob()}")
  Job getJob();

  @Value("#{target.getRequirement().getLocation()}")
  String getLocation();
}
