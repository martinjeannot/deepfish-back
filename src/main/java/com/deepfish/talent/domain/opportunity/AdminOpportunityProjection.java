package com.deepfish.talent.domain.opportunity;

import com.deepfish.employer.domain.Requirement;
import com.deepfish.user.domain.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "admin", types = {Opportunity.class})
public interface AdminOpportunityProjection extends AdminItemOpportunityProjection {

  User getCreator();

  Requirement getRequirement();

  String getPitch();

  String getTalentDeclinationReason();

  String getEmployerDeclinationReason();
}
