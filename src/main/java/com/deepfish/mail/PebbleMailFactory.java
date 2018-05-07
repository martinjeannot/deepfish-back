package com.deepfish.mail;

import com.deepfish.talent.domain.Talent;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;

@Component
public class PebbleMailFactory implements MailFactory {

  private final PebbleEngine pebbleEngine = new PebbleEngine.Builder().build();

  private final PebbleTemplate talentWelcomeMailTemplate = pebbleEngine
      .getTemplate("mails/talent/welcome.html");

  // TALENT ========================================================================================

  @Override
  public Email getTalentWelcomeMail(Talent talent) {
    String subject = "Bienvenue chez Deepfish";
    Writer writer = new StringWriter();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    try {
      talentWelcomeMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    return EmailBuilder
        .startingBlank()
        .from("david@deepfish.fr")
        .to(talent.getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }
}
