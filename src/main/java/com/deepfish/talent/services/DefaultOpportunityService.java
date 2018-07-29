package com.deepfish.talent.services;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
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

  private final TalentService talentService;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultOpportunityService(OpportunityRepository opportunityRepository,
      TalentService talentService, MailService mailService, MailFactory mailFactory) {
    this.opportunityRepository = opportunityRepository;
    this.talentService = talentService;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public void declineInBulk(UUID talentId, String bulkDeclinationReason) {
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
    Talent talent = talentService.deactivate(talentId);
    // notify admins
    mailService.send(
        mailFactory.getAdminTalentDeactivationMail(talent, bulkDeclinationReason, companyNames));
  }
}
