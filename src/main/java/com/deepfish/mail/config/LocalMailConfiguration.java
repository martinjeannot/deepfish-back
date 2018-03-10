package com.deepfish.mail.config;

import com.deepfish.mail.MailService;
import com.deepfish.mail.simplejavamail.SJMMailService;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@Import(SimpleJavaMailSpringSupport.class)
public class LocalMailConfiguration {

  @Bean
  MailService mailService(Mailer mailer) {
    return new SJMMailService(mailer);
  }
}
