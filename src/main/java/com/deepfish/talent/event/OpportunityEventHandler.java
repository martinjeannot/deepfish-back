package com.deepfish.talent.event;

import static com.deepfish.talent.domain.opportunity.OpportunityStatus.ACCEPTED;
import static com.deepfish.talent.domain.opportunity.OpportunityStatus.DECLINED;
import static com.deepfish.talent.domain.opportunity.OpportunityStatus.PENDING;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
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

  private final MailService mailService;

  private final MailFactory mailFactory;

  public OpportunityEventHandler(
      MailService mailService,
      MailFactory mailFactory) {
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
      // [previous state] data gathering

      OpportunityStatus previousTalentStatus = getPreviousOpportunityStatus("talentStatus",
          opportunity);
      OpportunityStatus previousEmployerStatus = getPreviousOpportunityStatus("employerStatus",
          opportunity);

      // [previous state] data specific behavior

      // talent response
      if (PENDING.equals(previousTalentStatus)
          && USER_ACTIONED_OPPORTUNITY_STATUSES.contains(opportunity.getTalentStatus())) {
        opportunity.handleTalentResponse(opportunity.getTalentStatus(),
            opportunity.getTalentDeclinationReason(), false);
      }

      // talent forwarding
      if (Objects.isNull(previousEmployerStatus)
          && PENDING.equals(opportunity.getEmployerStatus())) {
        opportunity.forwardToEmployer();
      }

      // handling employer response
      if (PENDING.equals(previousEmployerStatus)
          && USER_ACTIONED_OPPORTUNITY_STATUSES.contains(opportunity.getEmployerStatus())) {
        opportunity.handleEmployerResponse(opportunity.getEmployerStatus(),
            opportunity.getEmployerDeclinationReason());
      }

      // talent retrieval
      if (Objects.nonNull(previousEmployerStatus)
          && Objects.isNull(opportunity.getEmployerStatus())) {
        opportunity.retrieveFromEmployer();
      }
    }
  }

  @HandleAfterSave
  public void onAfterSave(Opportunity opportunity) {
    if (Objects.nonNull(opportunity.getPreviousState())) {
      // [previous state] data gathering

      OpportunityStatus previousTalentStatus = getPreviousOpportunityStatus("talentStatus",
          opportunity);
      OpportunityStatus previousEmployerStatus = getPreviousOpportunityStatus("employerStatus",
          opportunity);

      // [previous state] data specific behavior

      if (PENDING.equals(previousTalentStatus)) {
        if (ACCEPTED.equals(opportunity.getTalentStatus())) {
          mailService.send(mailFactory.getAdminTalentAcceptedOpportunityMail(opportunity));
        } else if (DECLINED.equals(opportunity.getTalentStatus())) {
          mailService.send(mailFactory.getAdminTalentDeclinedOpportunityMail(opportunity));
        }
      }

      if (PENDING.equals(previousEmployerStatus)) {
        if (ACCEPTED.equals(opportunity.getEmployerStatus())) {
          mailService.send(mailFactory.getAdminEmployerAcceptedTalentMail(opportunity));
        } else if (DECLINED.equals(opportunity.getEmployerStatus())) {
          mailService.send(mailFactory.getAdminEmployerDeclinedTalentMail(opportunity));
        }
      } else if (ACCEPTED.equals(previousEmployerStatus)) {
        if (DECLINED.equals(opportunity.getEmployerStatus())) {
          mailService.send(mailFactory.getAdminEmployerDisqualifiedTalentMail(opportunity));
        }
      }
    }
  }

  private OpportunityStatus getPreviousOpportunityStatus(String fieldName,
      Opportunity opportunity) {
    if (opportunity.getPreviousState().containsKey(fieldName)
        && Objects.nonNull(opportunity.getPreviousState().get(fieldName))) {
      return OpportunityStatus.valueOf((String) opportunity.getPreviousState().get(fieldName));
    }
    return null;
  }
}
