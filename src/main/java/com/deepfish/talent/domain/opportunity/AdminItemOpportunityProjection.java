package com.deepfish.talent.domain.opportunity;

import com.deepfish.company.domain.Company;
import com.deepfish.talent.domain.Talent;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "admin-item", types = {Opportunity.class})
public interface AdminItemOpportunityProjection {

  UUID getId();

  LocalDateTime getCreatedAt();

  Talent getTalent();

  @Value("#{target.getRequirement().getCompany()}")
  Company getCompany();

  OpportunityStatus getStatus();

  boolean isForwarded();
}
