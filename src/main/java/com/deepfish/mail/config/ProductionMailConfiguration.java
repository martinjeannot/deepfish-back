package com.deepfish.mail.config;

import com.deepfish.mail.MailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProductionMailConfiguration {

  @Bean
  MailService mailService() {
    return null; // TODO Sendgrid mail service
  }
}
