package com.deepfish.mail.simplejavamail;

import com.deepfish.mail.MailService;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;

public class SJMMailService implements MailService {

  private final Mailer mailer;

  public SJMMailService(Mailer mailer) {
    this.mailer = mailer;
  }

  @Override
  public void send(Email email) {
    mailer.sendMail(email);
  }
}
