package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
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

  private final PebbleTemplate talentNewOpportunityMailTemplate = pebbleEngine
      .getTemplate("mails/talent/newOpportunity.html");

  private final PebbleTemplate employerWelcomeMailTemplate = pebbleEngine
      .getTemplate("mails/employer/welcome.html");

  private final PebbleTemplate employerPasswordResetMailTemplate = pebbleEngine
      .getTemplate("mails/employer/passwordReset.html");

  private final PebbleTemplate adminNewEmployerMailTemplate = pebbleEngine
      .getTemplate("mails/admin/newEmployer.html");

  private final PebbleTemplate adminTalentAcceptedOpportunityMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentAcceptedOpportunity.html");

  private final PebbleTemplate adminTalentDeclinedOpportunityMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentDeclinedOpportunity.html");

  private final PebbleTemplate adminEmployerAcceptedTalentMailTemplate = pebbleEngine
      .getTemplate("mails/admin/employerAcceptedTalent.html");

  private final PebbleTemplate adminEmployerDeclinedTalentMailTemplate = pebbleEngine
      .getTemplate("mails/admin/employerDeclinedTalent.html");

  // TALENT ========================================================================================

  @Override
  public Email getTalentWelcomeMail(Talent talent) {
    String subject = "Bienvenue chez Deepfish";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    Writer writer = new StringWriter();
    try {
      talentWelcomeMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from("david@deepfish.fr")
        .to(talent.getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  @Override
  public Email getTalentNewOpportunityMail(Opportunity opportunity) {
    String subject =
        opportunity.getTalent().getFirstName() + ", une entreprise vous sollicite sur Deepfish";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", opportunity.getTalent());
    Writer writer = new StringWriter();
    try {
      talentNewOpportunityMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .to(opportunity.getTalent().getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  // EMPLOYER ======================================================================================


  @Override
  public Email getEmployerWelcomeMail(Employer employer, String password) {
    String subject = "Deepfish - Plateforme de recrutement de commerciaux IT";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("employer", employer);
    context.put("password", password);
    Writer writer = new StringWriter();
    try {
      employerWelcomeMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from("martin@deepfish.fr")
        .to(employer.getUsername())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  @Override
  public Email getEmployerPasswordResetMail(Employer employer, String password) {
    String subject = "Deepfish – Changement de mot de passe";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("employer", employer);
    context.put("password", password);
    Writer writer = new StringWriter();
    try {
      employerPasswordResetMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .to(employer.getUsername())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  // ADMIN =========================================================================================


  @Override
  public Email getAdminNewEmployerMail(Employer employer) {
    String subject = employer.getCompany().getName() + " - Nouvel inscrit recruteur";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("employer", employer);
    context.put("company", employer.getCompany());
    Writer writer = new StringWriter();
    try {
      adminNewEmployerMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple("david@deepfish.fr", "martin@deepfish.fr")
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  @Override
  public Email getAdminTalentAcceptedOpportunityMail(Opportunity opportunity) {
    String subject =
        opportunity.getTalent().getLastName() + " a accepté " + opportunity.getRequirement()
            .getCompany().getName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", opportunity.getTalent());
    context.put("company", opportunity.getRequirement().getCompany());
    Writer writer = new StringWriter();
    try {
      adminTalentAcceptedOpportunityMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple("david@deepfish.fr", "martin@deepfish.fr")
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  @Override
  public Email getAdminTalentDeclinedOpportunityMail(Opportunity opportunity) {
    String subject =
        opportunity.getTalent().getLastName() + " a refusé " + opportunity.getRequirement()
            .getCompany().getName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", opportunity.getTalent());
    context.put("company", opportunity.getRequirement().getCompany());
    context.put("opportunity", opportunity);
    Writer writer = new StringWriter();
    try {
      adminTalentDeclinedOpportunityMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple("david@deepfish.fr", "martin@deepfish.fr")
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  @Override
  public Email getAdminEmployerAcceptedTalentMail(Opportunity opportunity) {
    String subject =
        opportunity.getRequirement().getCompany().getName() + " a accepté le talent " + opportunity
            .getTalent().getLastName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("company", opportunity.getRequirement().getCompany());
    context.put("talent", opportunity.getTalent());
    Writer writer = new StringWriter();
    try {
      adminEmployerAcceptedTalentMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple("david@deepfish.fr", "martin@deepfish.fr")
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  @Override
  public Email getAdminEmployerDeclinedTalentMail(Opportunity opportunity) {
    String subject =
        opportunity.getRequirement().getCompany().getName() + " a refusé le talent " + opportunity
            .getTalent().getLastName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("company", opportunity.getRequirement().getCompany());
    context.put("talent", opportunity.getTalent());
    context.put("opportunity", opportunity);
    Writer writer = new StringWriter();
    try {
      adminEmployerDeclinedTalentMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple("david@deepfish.fr", "martin@deepfish.fr")
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }
}
