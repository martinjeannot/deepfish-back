package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.security.Role;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import org.simplejavamail.email.Email;
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

  private final MailFactory mailFactory;

  public DefaultEmployerService(
      EmployerRepository employerRepository,
      PasswordEncoder passwordEncoder,
      MailService mailService,
      MailFactory mailFactory) {
    this.employerRepository = employerRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public Employer create(Employer employer) {
    // encode raw password
    employer.setPassword(passwordEncoder.encode(employer.getPassword()));

    // set permissions
    employer.setAuthorities(
        Arrays
            .asList(Role.ROLE_USER.toGrantedAuthority(), Role.ROLE_EMPLOYER.toGrantedAuthority()));

    return employerRepository.save(employer);
  }

  @Override
  public Employer signUp(Employer employer, boolean fromTypeform) {
    // generate random password
    String password = generateRandomPassword();
    employer.setPassword(password);

    // allow new employer to authenticate
    employer.enableAuthentication();

    create(employer);

    // send employer welcome mail
    Email employerWelcomeMail =
        fromTypeform ? mailFactory.getEmployerWelcomeFromTypeformMail(employer, password)
            : mailFactory.getEmployerWelcomeMail(employer, password);
    mailService.send(employerWelcomeMail);

    // send admin notification mail
    Email adminNewEmployerMail = mailFactory.getAdminNewEmployerMail(employer);
    mailService.send(adminNewEmployerMail);

    return employer;
  }

  @Override
  public boolean resetPassword(String email) {
    Employer employer = employerRepository.findByUsername(email);
    if (Objects.isNull(employer)) {
      return false;
    }

    String newPassword = generateRandomPassword();
    employer.setPassword(passwordEncoder.encode(newPassword));
    employerRepository.save(employer);

    Email passwordResetMail = mailFactory.getEmployerPasswordResetMail(employer, newPassword);
    mailService.send(passwordResetMail);
    return true;
  }

  private String generateRandomPassword() {
    return UUID.randomUUID().toString().split("-")[0];
  }
}
