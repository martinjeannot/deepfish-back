package com.deepfish.user.services;

import com.deepfish.employer.repositories.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

  private final EmployerRepository employerRepository;

  @Autowired
  public DefaultUserDetailsService(EmployerRepository employerRepository) {
    this.employerRepository = employerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails userDetails = employerRepository.findByUsername(username);
    if (userDetails == null) {
      throw new UsernameNotFoundException(username);
    }
    return userDetails;
  }
}
