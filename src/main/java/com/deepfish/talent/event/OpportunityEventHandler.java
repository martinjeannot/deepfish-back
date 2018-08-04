package com.deepfish.talent.event;

import static com.deepfish.talent.domain.opportunity.OpportunityStatus.ACCEPTED;
import static com.deepfish.talent.domain.opportunity.OpportunityStatus.DECLINED;
import static com.deepfish.talent.domain.opportunity.OpportunityStatus.PENDING;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class OpportunityEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpportunityEventHandler.class);

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

  @HandleAfterSave
  public void onAfterSave(Opportunity opportunity) {
    if (Objects.nonNull(opportunity.getPreviousState())) {
      // [previous state] data gathering
      OpportunityStatus previousTalentStatus = null;
      if (opportunity.getPreviousState().containsKey("talentStatus")) {
        previousTalentStatus = OpportunityStatus
            .valueOf((String) opportunity.getPreviousState().get("talentStatus"));
      }
      OpportunityStatus previousEmployerStatus = null;
      if (opportunity.getPreviousState().containsKey("employerStatus")) {
        previousEmployerStatus = OpportunityStatus
            .valueOf((String) opportunity.getPreviousState().get("employerStatus"));
      }

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
}
