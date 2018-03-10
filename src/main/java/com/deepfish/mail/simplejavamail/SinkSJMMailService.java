package com.deepfish.mail.simplejavamail;

import static org.simplejavamail.util.ConfigLoader.Property.DEFAULT_TO_ADDRESS;
import static org.simplejavamail.util.ConfigLoader.Property.DEFAULT_TO_NAME;
import static org.simplejavamail.util.ConfigLoader.getProperty;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

public class SinkSJMMailService extends SJMMailService {

  public SinkSJMMailService(Mailer mailer) {
    super(mailer);
  }

  @Override
  public void send(Email email) {
    email = EmailBuilder.copying(email)
        .clearRecipients()
        .to(getProperty(DEFAULT_TO_NAME), (String) getProperty(DEFAULT_TO_ADDRESS))
        .buildEmail();
    super.send(email);
  }
}
