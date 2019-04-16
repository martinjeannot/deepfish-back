package com.deepfish.talent.services;

import com.deepfish.linkedin.domain.LiteProfile;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.security.Role;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.TalentMapper;
import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.qualification.Qualification;
import com.deepfish.talent.repositories.TalentRepository;
import com.deepfish.talent.repositories.UtmRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.simplejavamail.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * {@link TalentService} default implementation
 */
@Service
public class DefaultTalentService implements TalentService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTalentService.class);

  private final OpportunityService opportunityService;

  private final TalentRepository talentRepository;

  private final UtmRepository utmRepository;

  private final PasswordEncoder passwordEncoder;

  private final ObjectMapper objectMapper;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultTalentService(
      OpportunityService opportunityService,
      TalentRepository talentRepository,
      UtmRepository utmRepository,
      PasswordEncoder passwordEncoder,
      ObjectMapper objectMapper,
      MailService mailService,
      MailFactory mailFactory
  ) {
    this.opportunityService = opportunityService;
    this.talentRepository = talentRepository;
    this.utmRepository = utmRepository;
    this.passwordEncoder = passwordEncoder;
    this.objectMapper = objectMapper;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public Talent create(Talent talent) {
    // encode randomly generated password (note that we make no use of it)
    talent.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

    // init permissions
    talent.setAuthorities(
        Arrays.asList(Role.ROLE_USER.toGrantedAuthority(), Role.ROLE_TALENT.toGrantedAuthority()));

    // init conditions
    talent.setConditions(new Conditions());

    // init qualification
    talent.setQualification(new Qualification());

    return talentRepository.save(talent);
  }

  @Override
  public Talent activate(Talent talent) {
    if (!talent.isActive()) { // covering the cases where the update was already made via our API
      talent.activate();
      talent = talentRepository.save(talent);
    }
    // notify admins
    mailService.send(mailFactory.getAdminTalentActivationMail(talent));
    return talent;
  }

  @Override
  public Talent deactivate(UUID talentId, String deactivationReason) {
    Talent talent = talentRepository.findOne(talentId);
    return deactivate(talent, deactivationReason);
  }

  @Override
  public Talent deactivate(Talent talent, String deactivationReason) {
    List<String> companyNames = opportunityService
        .declineInBulk(talent.getId(), deactivationReason);
    if (talent.isActive()) { // covering the cases where the update was already made via our API
      talent.deactivate();
      talent = talentRepository.save(talent);
    }
    // notify admins
    mailService
        .send(mailFactory.getAdminTalentDeactivationMail(talent, deactivationReason, companyNames));
    return talent;
  }

  @Override
  public Talent signInFromLinkedIn(LiteProfile liteProfile, String emailAddress, UUID utmId) {
    // check if talent exists
    Talent talent = talentRepository.findByUsername(liteProfile.getId());
    if (talent == null) {
      talent = talentRepository.findByEmail(emailAddress);
    }
    if (talent == null) {
      talent = talentRepository.findByFirstNameAndLastName(
          liteProfile.getFirstName(),
          liteProfile.getLastName());
    }
    if (talent == null) {
      // sign up
      return signUpFromLinkedIn(liteProfile, emailAddress, utmId);
    } else {
      // update talent profile
      talent.setUsername(liteProfile.getId());
      talent.setLinkedinId(liteProfile.getId());
      talent.setLastName(liteProfile.getLastName());
      talent.setFirstName(liteProfile.getFirstName());
      try {
        talent.setLiteProfileText(objectMapper.writeValueAsString(liteProfile.getLiteProfile()));
      } catch (JsonProcessingException e) {
        LOGGER.error(e.getMessage(), e);
      }
      talent.setLastSignedInAt(LocalDateTime.now(Clock.systemUTC()));
      return talentRepository.save(talent);
    }
  }

  @Override
  public Talent signUpFromLinkedIn(LiteProfile liteProfile, String emailAddress, UUID utmId) {
    Talent talent = TalentMapper.INSTANCE.liteProfileToTalent(liteProfile);

    String liteProfileText = null;
    try {
      liteProfileText = objectMapper.writeValueAsString(liteProfile.getLiteProfile());
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
    }

    // set default values on sign up
    talent
        .setEmail(emailAddress)
        .setLiteProfileText(liteProfileText)
        .setFullProfileText(liteProfileText)
        .setPhoneNumber("null");

    // new talents are activated by default
    talent.activate();

    // allow new talent to authenticate
    talent.enableAuthentication();

    // assign UTM record if any
    if (Objects.nonNull(utmId)) {
      talent.setUtm(utmRepository.findOne(utmId));
    }

    talent = create(talent);

    Email welcomeMail = mailFactory.getTalentWelcomeMail(talent);
    mailService.send(welcomeMail);

    return talent;
  }

  @Override
  public float updateProfileCompleteness(UUID talentId) {
    Talent talent = talentRepository.findOne(talentId);
    talent.setProfileCompleteness(10); // TODO
    talent = talentRepository.save(talent);
    return talent.getProfileCompleteness();
  }
}
