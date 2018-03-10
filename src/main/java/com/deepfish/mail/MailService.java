package com.deepfish.mail;

import org.simplejavamail.email.Email;

public interface MailService {

  void send(Email email);
}
