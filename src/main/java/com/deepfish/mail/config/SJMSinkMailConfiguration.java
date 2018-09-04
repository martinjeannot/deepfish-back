package com.deepfish.mail.config;

import com.deepfish.mail.MailService;
import com.deepfish.mail.SJMMailService;
import com.deepfish.mail.SinkMailServiceDecorator;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "local"})
@Import(SimpleJavaMailSpringSupport.class)
public class SJMSinkMailConfiguration {

  @Bean
  MailService mailService(
      Mailer mailer,
      @Value("${deepfish.mail.sink.name}") String sinkName,
      @Value("${deepfish.mail.sink.address}") String sinkAddress) {
    return new SinkMailServiceDecorator(new SJMMailService(mailer), sinkName, sinkAddress);
  }
}
