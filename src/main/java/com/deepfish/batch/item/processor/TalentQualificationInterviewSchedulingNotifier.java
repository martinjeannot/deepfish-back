package com.deepfish.batch.item.processor;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.util.UUID;
import org.springframework.batch.item.ItemProcessor;

public class TalentQualificationInterviewSchedulingNotifier implements ItemProcessor<UUID, Talent> {

  private final TalentRepository talentRepository;

  private final MailFactory mailFactory;

  private final MailService mailService;

  public TalentQualificationInterviewSchedulingNotifier(
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    this.talentRepository = talentRepository;
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Talent process(UUID talentId) throws Exception {
    Talent talent = talentRepository.findOne(talentId);
    mailService.send(mailFactory.getTalentQualificationInterviewSchedulingMail(talent));
    return talent;
  }
}
