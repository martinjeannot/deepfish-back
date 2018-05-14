package com.deepfish.talent.services;

import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.domain.opportunity.QOpportunity;
import com.deepfish.talent.repositories.OpportunityRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultOpportunityService implements OpportunityService {

  private final OpportunityRepository opportunityRepository;

  private final TalentService talentService;

  public DefaultOpportunityService(
      OpportunityRepository opportunityRepository,
      TalentService talentService) {
    this.opportunityRepository = opportunityRepository;
    this.talentService = talentService;
  }

  @Override
  public void declineInBulk(UUID talentId) {
    // the number of pending opportunities per talent should not justify a batch update here
    QOpportunity opportunity = QOpportunity.opportunity;
    BooleanExpression fromTalent = opportunity.talent.id.eq(talentId);
    BooleanExpression isPending = opportunity.talentStatus.eq(OpportunityStatus.PENDING);
    opportunityRepository.findAll(fromTalent.and(isPending)).forEach(pendingOpportunity -> {
      pendingOpportunity.setTalentStatus(OpportunityStatus.DECLINED);
      opportunityRepository.save(pendingOpportunity);
    });
    talentService.deactivate(talentId);
  }
}
