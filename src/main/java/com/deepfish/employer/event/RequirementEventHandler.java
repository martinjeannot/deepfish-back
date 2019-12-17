package com.deepfish.employer.event;

import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RequirementEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequirementEventHandler.class);

  private final MailService mailService;

  private final MailFactory mailFactory;

  public RequirementEventHandler(MailService mailService, MailFactory mailFactory) {
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @HandleAfterCreate
  public void onAfterCreate(Requirement requirement) {
    // disabling new requirement notification (V3 debut : only admins can create new requirements)
    // to be reactivated when employers can create their own requirements
    // mailService.send(mailFactory.getAdminNewRequirementMail(requirement));
  }
}
