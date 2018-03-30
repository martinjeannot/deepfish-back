package com.deepfish.mail;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * SendGrid mail service
 */
public class SendGridMailService extends AbstractMailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SendGridMailService.class);

  private final SendGrid sendGridClient;

  public SendGridMailService(String sendGridApiKey) {
    sendGridClient = new SendGrid(sendGridApiKey);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }

  @Override
  protected void sendMail(org.simplejavamail.email.Email email) {
    Mail mail = new Mail();
    mail.setFrom(
        new Email(email.getFromRecipient().getAddress(), email.getFromRecipient().getName()));
    mail.setSubject(email.getSubject());
    Personalization personalization = new Personalization();
    email.getRecipients().forEach(
        recipient -> personalization.addTo(new Email(recipient.getAddress(), recipient.getName())));
    mail.addPersonalization(personalization);
    Content content = StringUtils.isEmpty(email.getPlainText())
        ? new Content("text/html", email.getHTMLText())
        : new Content("text/plain", email.getPlainText());
    mail.addContent(content);

    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");

    try {
      request.setBody(mail.build());
      Response response = sendGridClient.api(request);
      if (response.getStatusCode() != 202) {
        LOGGER.error(response.getBody());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
