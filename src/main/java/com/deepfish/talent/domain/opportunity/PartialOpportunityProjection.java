package com.deepfish.talent.domain.opportunity;

import com.deepfish.talent.domain.conditions.Job;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "partial", types = {Opportunity.class})
public interface PartialOpportunityProjection {

  UUID getId();

  OpportunityStatus getStatus();

  @Value("#{target.getRequirement().getCompany().getName()}")
  String getCompanyName();

  @Value("#{target.getRequirement().getJob()}")
  Job getJob();
}
