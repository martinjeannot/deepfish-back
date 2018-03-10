package com.deepfish.mail.config;

import com.deepfish.mail.MailService;
import com.deepfish.mail.simplejavamail.SinkSJMMailService;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("staging")
@Import(SimpleJavaMailSpringSupport.class)
public class StagingMailConfiguration {

  @Bean
  MailService mailService(Mailer mailer) {
    return new SinkSJMMailService(mailer);
  }
}
