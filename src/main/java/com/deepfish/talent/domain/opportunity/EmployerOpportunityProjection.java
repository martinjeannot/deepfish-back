package com.deepfish.talent.domain.opportunity;

import com.deepfish.employer.domain.Requirement;
import com.deepfish.talent.domain.Talent;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer", types = {Opportunity.class})
public interface EmployerOpportunityProjection {

  Requirement getRequirement();

  Talent getTalent();
}
