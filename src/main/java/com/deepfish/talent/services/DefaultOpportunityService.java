package com.deepfish.talent.services;

import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.domain.opportunity.QOpportunity;
import com.deepfish.talent.repositories.OpportunityRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultOpportunityService implements OpportunityService {

  private final OpportunityRepository opportunityRepository;

  public DefaultOpportunityService(OpportunityRepository opportunityRepository) {
    this.opportunityRepository = opportunityRepository;
  }

  @Override
  public List<String> declineInBulk(UUID talentId, String bulkDeclinationReason) {
    List<String> companyNames = new ArrayList<>();
    // the number of pending opportunities per talent should not justify a batch update here
    QOpportunity opportunity = QOpportunity.opportunity;
    BooleanExpression fromTalent = opportunity.talent.id.eq(talentId);
    BooleanExpression isPending = opportunity.talentStatus.eq(OpportunityStatus.PENDING);
    opportunityRepository.findAll(fromTalent.and(isPending)).forEach(pendingOpportunity -> {
      pendingOpportunity.setTalentStatus(OpportunityStatus.DECLINED);
      pendingOpportunity.setTalentDeclinationReason(bulkDeclinationReason);
      opportunityRepository.save(pendingOpportunity);
      companyNames.add(pendingOpportunity.getRequirement().getCompany().getName());
    });
    return companyNames;
  }
}
