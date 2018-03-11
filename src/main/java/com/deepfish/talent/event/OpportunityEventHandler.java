package com.deepfish.talent.event;

import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
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
        .withSubject("Vous avez reçu une nouvelle opportunité !")
        .withPlainText("La société " + opportunity.getRequirement().getCompany().getName()
            + " pourrait éventuellement dans un futur plus ou moins proche vouloir entrer en contact avec vous !")
        .buildEmail();
    mailService.send(opportunityMail);
  }
}
