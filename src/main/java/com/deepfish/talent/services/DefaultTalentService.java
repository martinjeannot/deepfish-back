package com.deepfish.talent.services;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.security.Role;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.TalentMapper;
import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.qualification.Qualification;
import com.deepfish.talent.repositories.TalentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
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

  private final TalentRepository talentRepository;

  private final PasswordEncoder passwordEncoder;

  private final ObjectMapper objectMapper;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultTalentService(
      TalentRepository talentRepository,
      PasswordEncoder passwordEncoder,
      ObjectMapper objectMapper,
      MailService mailService,
      MailFactory mailFactory) {
    this.talentRepository = talentRepository;
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
  public Talent deactivate(UUID talentId) {
    Talent talent = talentRepository.findById(talentId).orElse(null);
    talent.deactivate();
    return talentRepository.save(talent);
  }

  @Override
  public Talent signInFromLinkedin(Map<String, Object> basicProfile) {
    // check if talent exists
    Talent talent = talentRepository.findByUsername((String) basicProfile.get("id"));
    if (talent == null) {
      talent = talentRepository.findByEmail((String) basicProfile.get("emailAddress"));
    }
    if (talent == null) {
      talent = talentRepository.findByFirstNameAndLastName(
          (String) basicProfile.get("firstName"),
          (String) basicProfile.get("lastName"));
    }
    if (talent == null) {
      // sign up
      return signUpFromLinkedIn(basicProfile);
    } else {
      // update talent profile
      talent.setUsername((String) basicProfile.get("id"));
      talent.setLinkedInId((String) basicProfile.get("id"));
      talent.setLastName((String) basicProfile.get("lastName"));
      talent.setFirstName((String) basicProfile.get("firstName"));
      talent.setBasicProfile(basicProfile);
      try {
        talent.setBasicProfileText(objectMapper.writeValueAsString(basicProfile));
      } catch (JsonProcessingException e) {
        LOGGER.error(e.getMessage(), e);
        talent.setBasicProfileText("{}");
      }
      talent.setLastSignedInAt(LocalDateTime.now());
      return talentRepository.save(talent);
    }
  }

  @Override
  public Talent signUpFromLinkedIn(Map<String, Object> basicProfile) {
    Talent talent = TalentMapper.INSTANCE.mapToTalent(basicProfile);

    String basicProfileText = "{}";
    try {
      basicProfileText = objectMapper.writeValueAsString(basicProfile);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
    }

    // set default values on sign up
    talent
        .setBasicProfileText(basicProfileText)
        .setFullProfileText(basicProfileText)
        .setPhoneNumber("null");

    // new talents are activated by default
    talent.activate();

    // allow new talent to authenticate
    talent.enableAuthentication();

    talent = create(talent);

    Email welcomeMail = mailFactory.getTalentWelcomeMail(talent);
    mailService.send(welcomeMail);

    return talent;
  }

  @Override
  public float updateProfileCompleteness(UUID talentId) {
    Talent talent = talentRepository.findById(talentId).orElse(null);
    talent.setProfileCompleteness(10); // TODO
    talent = talentRepository.save(talent);
    return talent.getProfileCompleteness();
  }
}
