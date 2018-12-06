package com.deepfish.batch.item.processor;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import org.springframework.batch.item.ItemProcessor;

public class IncompleteProfileNotifier implements ItemProcessor<Talent, Talent> {

  private final MailFactory mailFactory;

  private final MailService mailService;

  public IncompleteProfileNotifier(
      MailFactory mailFactory,
      MailService mailService
  ) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Talent process(Talent talent) throws Exception {
    talent.calculateProfileCompleteness();
    if (talent.getProfileCompleteness() <= 60) { // incomplete profile
      mailService.send(
          mailFactory.getTalentIncompleteProfile1stMail(talent));
    }
    return talent;
  }
}
