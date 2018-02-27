package com.deepfish.talent.domain.opportunity;

import com.deepfish.employer.domain.Requirement;
import com.deepfish.talent.domain.Talent;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer-talent", types = {Opportunity.class})
public interface EmployerTalentOpportunityProjection {

  Requirement getRequirement();

  Talent getTalent();
}
