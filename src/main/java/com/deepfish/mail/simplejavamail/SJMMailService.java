package com.deepfish.mail.simplejavamail;

import com.deepfish.mail.MailService;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SJMMailService implements MailService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final Mailer mailer;

  public SJMMailService(Mailer mailer) {
    this.mailer = mailer;
  }

  @Override
  public void send(Email email) {
    try {
      mailer.sendMail(email);
    } catch (Exception e) { // FIXME room for improvement
      logger.error(e.getMessage(), e);
    }
  }
}
