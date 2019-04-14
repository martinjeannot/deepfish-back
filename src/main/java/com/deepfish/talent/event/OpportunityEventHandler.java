package com.deepfish.talent.event;

import static com.deepfish.talent.domain.opportunity.OpportunityStatus.ACCEPTED;
import static com.deepfish.talent.domain.opportunity.OpportunityStatus.DECLINED;
import static com.deepfish.talent.domain.opportunity.OpportunityStatus.PENDING;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.services.OpportunityService;
import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class OpportunityEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpportunityEventHandler.class);

  private static final Set<OpportunityStatus> USER_ACTIONED_OPPORTUNITY_STATUSES = Sets
      .newHashSet(ACCEPTED, DECLINED);

  private final OpportunityService opportunityService;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public OpportunityEventHandler(
      OpportunityService opportunityService,
      MailService mailService,
      MailFactory mailFactory
  ) {
    this.opportunityService = opportunityService;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @HandleAfterCreate
  public void onAfterCreate(Opportunity opportunity) {
    mailService.send(mailFactory.getTalentNewOpportunityMail(opportunity));
  }

  @HandleBeforeSave
  public void onBeforeSave(Opportunity opportunity) {
    if (Objects.nonNull(opportunity.getPreviousState())) {

      // talent response
      if (PENDING.equals(opportunity.getPreviousTalentStatus())
          && USER_ACTIONED_OPPORTUNITY_STATUSES.contains(opportunity.getTalentStatus())) {
        opportunity.handleTalentResponse(
            opportunity.getTalentStatus(),
            opportunity.getTalentDeclinationReason(),
            false);
      }

      // talent forwarding
      if (Objects.isNull(opportunity.getPreviousEmployerStatus())
          && PENDING.equals(opportunity.getEmployerStatus())) {
        opportunity.forwardToEmployer();
      }

      // handling employer response
      if (PENDING.equals(opportunity.getPreviousEmployerStatus())
          && USER_ACTIONED_OPPORTUNITY_STATUSES.contains(opportunity.getEmployerStatus())) {
        opportunity.handleEmployerResponse(
            opportunity.getEmployerStatus(),
            opportunity.getEmployerDeclinationReason());
      }

      // talent retrieval
      if (Objects.nonNull(opportunity.getPreviousEmployerStatus())
          && Objects.isNull(opportunity.getEmployerStatus())) {
        opportunity.retrieveFromEmployer();
      }
    }
  }

  @HandleAfterSave
  public void onAfterSave(Opportunity opportunity) {
    if (Objects.nonNull(opportunity.getPreviousState())) {

      // check for talent status change
      if (PENDING.equals(opportunity.getPreviousTalentStatus())) {
        if (ACCEPTED.equals(opportunity.getTalentStatus())) {
          mailService.send(mailFactory.getAdminTalentAcceptedOpportunityMail(opportunity));
        } else if (DECLINED.equals(opportunity.getTalentStatus())) {
          mailService.send(mailFactory.getAdminTalentDeclinedOpportunityMail(opportunity));
        }
      }

      // check for employer status change
      if (Objects.nonNull(opportunity.getPreviousEmployerStatus())
          && !opportunity.getEmployerStatus().equals(opportunity.getPreviousEmployerStatus())) {
        switch (opportunity.getEmployerStatus()) {
          case ACCEPTED:
            mailService.send(mailFactory.getAdminEmployerAcceptedTalentMail(opportunity));
            break;
          case DECLINED:
            opportunityService.handleEmployerDeclination(opportunity);
            break;
          default:
            break;
        }
      }
    }
  }
}
