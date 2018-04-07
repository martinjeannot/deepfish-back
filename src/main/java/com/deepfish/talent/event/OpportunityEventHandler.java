package com.deepfish.talent.event;

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

  private final MailService mailService;

  public OpportunityEventHandler(MailService mailService) {
    this.mailService = mailService;
  }

  @HandleAfterCreate
  public void onAfterCreate(Opportunity opportunity) {
    Email opportunityMail = EmailBuilder.startingBlank()
        .to(opportunity.getTalent().getEmail())
        .withSubject("Vous avez reçu une nouvelle opportunité sur Deepfish !")
        .withPlainText("La société " + opportunity.getRequirement().getCompany().getName()
            + " pourrait être intéressée par votre profil !\n\n"
            + opportunity.getPitch() + "\n\nQuelques mots sur la société : \n"
            + opportunity.getRequirement().getCompany().getDescription() + "\n\n"
            + "N'oubliez pas de répondre sur Deepfish !")
        .buildEmail();
    mailService.send(opportunityMail);
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
