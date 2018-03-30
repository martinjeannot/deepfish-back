package com.deepfish.mail;

import org.simplejavamail.email.Email;
import org.slf4j.Logger;

public abstract class AbstractMailService implements MailService {

  protected abstract Logger getLogger();

  protected abstract void sendMail(Email email);

  @Override
  public void send(Email email) {
    try {
      sendMail(email);
    } catch (Exception e) { // safety net
      getLogger().error(e.getMessage(), e);
    }
  }
}
