package com.deepfish.batch.item.processor;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.batch.item.ItemProcessor;

public class TalentReactivator implements ItemProcessor<Talent, Talent> {

  private final LocalDate now = LocalDate.now(Clock.systemUTC());

  private final MailFactory mailFactory;

  private final MailService mailService;

  public TalentReactivator(
      MailFactory mailFactory,
      MailService mailService
  ) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Talent process(Talent talent) throws Exception {
    if (!talent.isActive() && now.isAfter(talent.getReactivatedOn())) {
      talent.activate();
      talent.setReactivatedOn(null);
      mailService.send(mailFactory.getAdminTalentReactivationMail(talent));
    }
    return talent;
  }
}
