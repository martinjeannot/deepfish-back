package com.deepfish.user.services;

import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

  private final EmployerRepository employerRepository;

  private final UserRepository userRepository;

  @Autowired
  public DefaultUserDetailsService(EmployerRepository employerRepository,
      UserRepository userRepository) {
    this.employerRepository = employerRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails userDetails = employerRepository.findByUsername(username);
    userDetails = userDetails == null ? userRepository.findByUsername(username) : userDetails;
    if (userDetails == null) {
      throw new UsernameNotFoundException(username);
    }
    return userDetails;
  }
}
