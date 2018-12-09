package com.deepfish.batch.item.processor;

import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.batch.item.ItemProcessor;

public class OpportunityExpirator implements ItemProcessor<Opportunity, Opportunity> {

  private final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

  @Override
  public Opportunity process(Opportunity opportunity) throws Exception {
    if (OpportunityStatus.PENDING.equals(opportunity.getTalentStatus())
        && now.isAfter(opportunity.getCreatedAt().plusDays(Opportunity.LIFESPAN_DAYS))) {
      opportunity.setTalentStatus(OpportunityStatus.EXPIRED);
    }
    return opportunity;
  }
}
