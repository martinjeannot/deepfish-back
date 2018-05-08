package com.deepfish.talent.event;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import java.util.Objects;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
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

  private final MailFactory mailFactory;

  private final MailService mailService;

  public OpportunityEventHandler(
      MailFactory mailFactory,
      MailService mailService) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
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

      // [previous state] data specific behavior
      if (OpportunityStatus.ACCEPTED.equals(opportunity.getTalentStatus())
          && OpportunityStatus.PENDING.equals(previousTalentStatus)) {
        Email acceptedOpportunityNotification = EmailBuilder
            .startingBlank()
            .toMultiple("david@deepfish.fr", "martin@deepfish.fr")
            .withSubject("[Deepfish] Accepted opportunity")
            .withPlainText(
                "Company : "
                    + opportunity.getRequirement().getCompany().getName()
                    + "\n\nTalent : "
                    + opportunity.getTalent().getLastName()
                    + " "
                    + opportunity.getTalent().getFirstName())
            .buildEmail();
        mailService.send(acceptedOpportunityNotification);
      }
    }
  }
}
