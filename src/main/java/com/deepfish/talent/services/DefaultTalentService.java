package com.deepfish.talent.services;

import com.deepfish.company.repositories.CompanyMaturityLevelRepository;
import com.deepfish.security.Role;
import com.deepfish.talent.domain.TalentMaturityLevel;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.TalentMapper;
import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.repositories.TalentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private ObjectMapper objectMapper;

  public DefaultTalentService(
      TalentRepository talentRepository,
      PasswordEncoder passwordEncoder,
      CompanyMaturityLevelRepository companyMaturityLevelRepository) {
    this.talentRepository = talentRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void create(Talent talent) {
    // encode randomly generated password (note that we make no use of it)
    talent.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

    // set permissions
    talent.setAuthorities(
        Arrays.asList(Role.ROLE_USER.toGrantedAuthority(), Role.ROLE_TALENT.toGrantedAuthority()));

    // set conditions
    talent.setConditions(new Conditions());

    talentRepository.save(talent);
  }

  @Override
  public void signUpFromLinkedIn(Map<String, Object> profile) {
    Talent talent = TalentMapper.INSTANCE.mapToTalent(profile);

    // set default values on sign up
    talent
        .setMaturityLevel(TalentMaturityLevel.CLEAR_WATER)
        .setPhoneNumber("null");

    // allow new talent to authenticate
    talent.enableAuthentication();

    create(talent);
  }
}
