package com.deepfish.talent.services;

import com.deepfish.company.repositories.CompanyMaturityLevelRepository;
import com.deepfish.security.Role;
import com.deepfish.talent.domain.Conditions;
import com.deepfish.talent.domain.MaturityLevel;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * {@link TalentService} default implementation
 */
@Service
public class DefaultTalentService implements TalentService {

  private final TalentRepository talentRepository;

  private final PasswordEncoder passwordEncoder;

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
    Conditions conditions = new Conditions();
    talent.setConditions(conditions); // cascade persist
    conditions.setTalent(talent); // because child gets its id from parent

    talentRepository.save(talent);
  }

  @Override
  public void signUp(Talent talent) {
    // set default values on sign up
    talent
        .setMaturityLevel(MaturityLevel.CLEAR_WATER)
        .setPhoneNumber("null");

    // allow new talent to authenticate
    talent.enableAuthentication();

    create(talent);
  }
}
