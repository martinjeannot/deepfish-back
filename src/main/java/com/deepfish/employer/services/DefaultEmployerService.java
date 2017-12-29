package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.repositories.EmployerRepository;
import java.util.UUID;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * {@link EmployerService} default implementation
 */
@Service
public class DefaultEmployerService implements EmployerService {

  private final EmployerRepository employerRepository;

  private final MailSender mailSender;

  private final PasswordEncoder passwordEncoder;

  public DefaultEmployerService(EmployerRepository employerRepository, MailSender mailSender,
      PasswordEncoder passwordEncoder) {
    this.employerRepository = employerRepository;
    this.mailSender = mailSender;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void create(Employer employer) {
    // encode raw password
    employer.setPassword(passwordEncoder.encode(employer.getPassword()));

    employerRepository.save(employer);
  }

  @Override
  public void signUp(Employer employer) {
    // generate random password
    String password = UUID.randomUUID().toString().split("-")[0];
    employer.setPassword(password);

    create(employer);

    // send confirmation mail
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setFrom("morgandeepfish@gmail.com");
    mail.setTo("m4rtinjeannot@gmail.com");
    mail.setSubject("Confirmation de votre inscription");
    mail.setText("Merci de vous être inscrit !!!");
    try {
      mailSender.send(mail);
    } catch (MailException e) {
      System.err.println(e.getMessage());
    }
  }
}
