package com.deepfish.talent.services;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * {@link ReferralService} default implementation
 */
@Service
public class DefaultReferralService implements ReferralService {

  private final TalentRepository talentRepository;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultReferralService(
      TalentRepository talentRepository,
      MailService mailService,
      MailFactory mailFactory
  ) {
    this.talentRepository = talentRepository;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public void sendInvitations(
      UUID talentId,
      String emails,
      String subject,
      String message
  ) {
    Talent talent = talentRepository.findOne(talentId);
    mailService
        .send(mailFactory.getAdminTalentReferralInvitationMail(talent, emails, subject, message));
  }
}
