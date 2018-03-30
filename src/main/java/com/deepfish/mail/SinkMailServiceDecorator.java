package com.deepfish.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;

/**
 * Mail service decorator used to redirect all mails to a specific mail address
 */
public class SinkMailServiceDecorator implements MailService {

  private final MailService mailService;

  private final String sinkName;

  private final String sinkAddress;

  public SinkMailServiceDecorator(MailService mailService, String sinkName, String sinkAddress) {
    this.mailService = mailService;
    this.sinkName = sinkName;
    this.sinkAddress = sinkAddress;
  }

  @Override
  public void send(Email email) {
    // clearing actual recipient(s) before adding the sink one
    email = EmailBuilder.copying(email)
        .clearRecipients()
        .to(sinkName, sinkAddress)
        .buildEmail();
    mailService.send(email);
  }
}
