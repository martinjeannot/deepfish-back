package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.mail.MailService;
import com.deepfish.security.Role;
import java.util.Arrays;
import java.util.UUID;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * {@link EmployerService} default implementation
 */
@Service
public class DefaultEmployerService implements EmployerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEmployerService.class);

  private final EmployerRepository employerRepository;

  private final PasswordEncoder passwordEncoder;

  private final MailService mailService;

  public DefaultEmployerService(
      EmployerRepository employerRepository,
      PasswordEncoder passwordEncoder,
      MailService mailService) {
    this.employerRepository = employerRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailService = mailService;
  }

  @Override
  public void create(Employer employer) {
    // encode raw password
    employer.setPassword(passwordEncoder.encode(employer.getPassword()));

    // set permissions
    employer.setAuthorities(
        Arrays
            .asList(Role.ROLE_USER.toGrantedAuthority(), Role.ROLE_EMPLOYER.toGrantedAuthority()));

    employerRepository.save(employer);
  }

  @Override
  public void signUp(Employer employer) {
    // generate random password
    String password = UUID.randomUUID().toString().split("-")[0];
    employer.setPassword(password);
    LOGGER.error(password);

    // allow new employer to authenticate
    employer.enableAuthentication();

    create(employer);

    // send confirmation mail
    Email email = EmailBuilder
        .startingBlank()
        .to(employer.getUsername())
        .withSubject("Confirmation de votre inscription")
        .withPlainText("Bravo, vous êtes inscrit !\n\nVoici votre mot de passe : " + password
            + "\n\nLa team Deepfish")
        .buildEmail();
    mailService.send(email);
  }
}
