package com.deepfish.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Java Mail mail service
 */
public class SJMMailService extends AbstractMailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SJMMailService.class);

  private final Mailer mailer;

  public SJMMailService(Mailer mailer) {
    this.mailer = mailer;
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }

  @Override
  protected void sendMail(Email email) {
    mailer.sendMail(email);
  }
}
