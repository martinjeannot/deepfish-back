package com.deepfish.talent.domain.opportunity;

import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.user.domain.User;
import java.time.LocalDateTime;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "admin", types = {Opportunity.class})
public interface AdminOpportunityProjection extends AdminItemOpportunityProjection {

  User getCreator();

  Requirement getRequirement();

  String getPitch();

  LocalDateTime getTalentRespondedAt();

  LocalDateTime getForwardedAt();

  LocalDateTime getEmployerRespondedAt();
}
