package com.deepfish.mail.config;

import com.deepfish.mail.MailService;
import com.deepfish.mail.SendGridMailService;
import com.deepfish.mail.SinkMailServiceDecorator;
import java.util.Properties;
import org.simplejavamail.util.ConfigLoader;
import org.simplejavamail.util.ConfigLoader.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@Profile("staging")
public class SendGridSinkMailConfiguration {

  @Value("${simplejavamail.defaults.from.name:#{null}}")
  private String defaultFromName;

  @Value("${simplejavamail.defaults.from.address:#{null}}")
  private String defaultFromAddress;

  @EventListener(ContextRefreshedEvent.class)
  public void onContextRefreshedEvent() {
    final Properties emailProperties = new Properties();
    setNullableProperty(emailProperties, Property.DEFAULT_FROM_NAME.key(), defaultFromName);
    setNullableProperty(emailProperties, Property.DEFAULT_FROM_ADDRESS.key(), defaultFromAddress);
    ConfigLoader.loadProperties(emailProperties, true);
  }

  @Bean
  MailService mailService(
      @Value("${spring.sendgrid.api-key}") String sendGridApiKey,
      @Value("${deepfish.mail.sink.name}") String sinkName,
      @Value("${deepfish.mail.sink.address}") String sinkAddress) {
    return new SinkMailServiceDecorator(new SendGridMailService(sendGridApiKey), sinkName,
        sinkAddress);
  }

  private static void setNullableProperty(final Properties emailProperties, final String key,
      final String value) {
    if (value != null) {
      emailProperties.setProperty(key, value);
    }
  }
}
