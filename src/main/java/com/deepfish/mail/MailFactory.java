package com.deepfish.mail;

import com.deepfish.talent.domain.Talent;
import org.simplejavamail.email.Email;

public interface MailFactory {

  // TALENT ========================================================================================

  Email getTalentWelcomeMail(Talent talent);
}
