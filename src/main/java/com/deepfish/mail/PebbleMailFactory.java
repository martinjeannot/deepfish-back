package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;

@Component
public class PebbleMailFactory implements MailFactory {

  private static final String DAVID_EMAIL = "david@deepfish.fr";

  private static final String LUIGI_EMAIL = "louisguillaume@deepfish.co";

  private final PebbleEngine pebbleEngine = new PebbleEngine.Builder().build();

  // TALENT ========================================================================================

  private final PebbleTemplate talentWelcomeMailTemplate = pebbleEngine
      .getTemplate("mails/talent/welcome.html");

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

  private final PebbleTemplate talentNewOpportunityMailTemplate = pebbleEngine
      .getTemplate("mails/talent/newOpportunity.html");

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

  private final PebbleTemplate talentAcceptedOpportunityMailTemplate = pebbleEngine
      .getTemplate("mails/talent/acceptedOpportunity.html");

  @Override
  public Email getTalentAcceptedOpportunityMail(Opportunity opportunity) {
    String subject = "Deepfish - Vous avez accepté une opportunité";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", opportunity.getTalent());
    context.put("company", opportunity.getRequirement().getCompany());
    Writer writer = new StringWriter();
    try {
      talentAcceptedOpportunityMailTemplate.evaluate(writer, context);
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

  private final PebbleTemplate talentOpportunityPendingFor24hMailTemplate = pebbleEngine
      .getTemplate("mails/talent/opportunityPendingFor24h.html");

  @Override
  public Email getTalentOpportunityPendingFor24hMail(Opportunity opportunity) {
    String subject = "Opportunité en attente sur Deepfish";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", opportunity.getTalent());
    Writer writer = new StringWriter();
    try {
      talentOpportunityPendingFor24hMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(opportunity.getTalent().getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  // EMPLOYER ======================================================================================

  private final PebbleTemplate employerWelcomeMailTemplate = pebbleEngine
      .getTemplate("mails/employer/welcome.html");

  @Override
  public Email getEmployerWelcomeMail(Employer employer, String password) {
    String subject = "Deepfish - Plateforme de recrutement de commerciaux en startup";
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
        .from("david@deepfish.fr")
        .to(employer.getUsername())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate employerPasswordResetMailTemplate = pebbleEngine
      .getTemplate("mails/employer/passwordReset.html");

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

  private final PebbleTemplate adminNewEmployerMailTemplate = pebbleEngine
      .getTemplate("mails/admin/newEmployer.html");

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
        .toMultiple(DAVID_EMAIL, LUIGI_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentAcceptedOpportunityMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentAcceptedOpportunity.html");

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
        .toMultiple(DAVID_EMAIL, LUIGI_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentDeclinedOpportunityMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentDeclinedOpportunity.html");

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
        .toMultiple(DAVID_EMAIL, LUIGI_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminEmployerAcceptedTalentMailTemplate = pebbleEngine
      .getTemplate("mails/admin/employerAcceptedTalent.html");

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
        .toMultiple(DAVID_EMAIL, LUIGI_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminEmployerDeclinedTalentMailTemplate = pebbleEngine
      .getTemplate("mails/admin/employerDeclinedTalent.html");

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
        .toMultiple(DAVID_EMAIL, LUIGI_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminEmployerDisqualifiedTalentMailTemplate = pebbleEngine
      .getTemplate("mails/admin/employerDisqualifiedTalent.html");

  @Override
  public Email getAdminEmployerDisqualifiedTalentMail(Opportunity opportunity) {
    String subject =
        opportunity.getRequirement().getCompany().getName() + " a disqualifié le talent "
            + opportunity.getTalent().getLastName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("company", opportunity.getRequirement().getCompany());
    context.put("talent", opportunity.getTalent());
    context.put("opportunity", opportunity);
    Writer writer = new StringWriter();
    try {
      adminEmployerDisqualifiedTalentMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple(DAVID_EMAIL, LUIGI_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminEmployerRequestMailTemplate = pebbleEngine
      .getTemplate("mails/admin/employerRequest.html");

  @Override
  public Email getAdminEmployerRequestMail(Employer employer, Talent talent, String message) {
    String subject =
        employer.getCompany().getName() + " a des questions sur " + talent.getLastName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("employer", employer);
    context.put("talent", talent);
    context.put("message", message);
    Writer writer = new StringWriter();
    try {
      adminEmployerRequestMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple(DAVID_EMAIL, LUIGI_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminOpportunitiesPendingFor48hMailTemplate = pebbleEngine
      .getTemplate("mails/admin/opportunitiesPendingFor48h.html");

  @Override
  public Email getAdminOpportunitiesPendingFor48hMail(Collection<String> talents) {
    String subject = "Relances MDEA par LKD du " + LocalDate.now().toString();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talents", talents);
    Writer writer = new StringWriter();
    try {
      adminOpportunitiesPendingFor48hMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .to(DAVID_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentPendingOpportunitiesFollowUpSMSMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentPendingOpportunitiesFollowUpSMS.html");

  @Override
  public Email getAdminTalentPendingOpportunitiesFollowUpSMSMail(Collection<String> talents) {
    String subject = "[Pending opportunities follow-up] SMS - " + LocalDate.now().toString();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talents", talents);
    Writer writer = new StringWriter();
    try {
      adminTalentPendingOpportunitiesFollowUpSMSMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .to(DAVID_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }
}
