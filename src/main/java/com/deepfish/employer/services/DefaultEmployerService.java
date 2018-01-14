package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.security.Role;
import java.util.Arrays;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
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

  private final MailSender mailSender;

  public DefaultEmployerService(
      EmployerRepository employerRepository,
      PasswordEncoder passwordEncoder,
      MailSender mailSender) {
    this.employerRepository = employerRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailSender = mailSender;
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
    /*SimpleMailMessage mail = new SimpleMailMessage();
    mail.setFrom("morgandeepfish@gmail.com");
    mail.setTo("m4rtinjeannot@gmail.com");
    mail.setSubject("Confirmation de votre inscription");
    mail.setText("Merci de vous être inscrit !!!");
    try {
      mailSender.send(mail);
    } catch (MailException e) {
      System.err.println(e.getMessage());
    }*/
  }
}
