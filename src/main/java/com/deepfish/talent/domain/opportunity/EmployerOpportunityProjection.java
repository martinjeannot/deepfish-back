package com.deepfish.talent.domain.opportunity;

import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.interview.domain.Interview;
import com.deepfish.talent.domain.EmployerTalentProjection;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer", types = {Opportunity.class})
public interface EmployerOpportunityProjection {

  UUID getId();

  EmployerTalentProjection getTalent();

  Requirement getRequirement();

  Set<Interview> getInterviews();

  OpportunityStatus getEmployerStatus();

  LocalDateTime getForwardedAt();
}
