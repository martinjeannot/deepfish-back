package com.deepfish.user.services;

import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.talent.repositories.TalentRepository;
import com.deepfish.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

  private final TalentRepository talentRepository;

  private final EmployerRepository employerRepository;

  private final UserRepository userRepository;

  @Autowired
  public DefaultUserDetailsService(
      TalentRepository talentRepository,
      EmployerRepository employerRepository,
      UserRepository userRepository) {
    this.talentRepository = talentRepository;
    this.employerRepository = employerRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails userDetails = talentRepository.findByUsername(username);
    userDetails = userDetails == null ? employerRepository.findByUsername(username) : userDetails;
    userDetails = userDetails == null ? userRepository.findByUsername(username) : userDetails;
    if (userDetails == null) {
      throw new UsernameNotFoundException(username);
    }
    return userDetails;
  }
}
