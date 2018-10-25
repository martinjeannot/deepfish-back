package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.mail.util.FrontAppUrlBuilder;
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
import java.util.List;
import java.util.Map;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;

@Component
public class PebbleMailFactory implements MailFactory {

  private static final String CEO_EMAIL = "david@deepfish.fr";

  private static final String[] SALES_TEAM_EMAILS = new String[]{"david@deepfish.fr",
      "bruno@deepfish.co", "axel@deepfish.co"};

  private final PebbleEngine pebbleEngine = new PebbleEngine.Builder().build();

  private final FrontAppUrlBuilder frontAppUrlBuilder;

  public PebbleMailFactory(FrontAppUrlBuilder frontAppUrlBuilder) {
    this.frontAppUrlBuilder = frontAppUrlBuilder;
  }

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
    String subject = opportunity.getTalent().getFirstName()
        + ", tu as reçu une nouvelle opportunité sur Deepfish";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", opportunity.getTalent());
    context.put("opportunityUrl", frontAppUrlBuilder.getTalentOpportunityUrl(opportunity));
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

  private final PebbleTemplate talentPendingOpportunityFollowUp2ndMailTemplate = pebbleEngine
      .getTemplate("mails/talent/pendingOpportunityFollowUp2nd.html");

  @Override
  public Email getTalentPendingOpportunityFollowUp2ndMail(Opportunity opportunity) {
    String subject = "RAPPEL - " + opportunity.getTalent().getFirstName()
        + ", une opportunité t'attend sur Deepfish !";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", opportunity.getTalent());
    Writer writer = new StringWriter();
    try {
      talentPendingOpportunityFollowUp2ndMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(CEO_EMAIL)
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

  private final PebbleTemplate employerWelcomeFromTypeformMailTemplate = pebbleEngine
      .getTemplate("mails/employer/welcomeFromTypeform.html");

  @Override
  public Email getEmployerWelcomeFromTypeformMail(Employer employer, String password) {
    String subject = "Deepfish - Plateforme de recrutement de commerciaux en startup";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("employer", employer);
    context.put("password", password);
    Writer writer = new StringWriter();
    try {
      employerWelcomeFromTypeformMailTemplate.evaluate(writer, context);
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
        .toMultiple(SALES_TEAM_EMAILS)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminNewRequirementMailTemplate = pebbleEngine
      .getTemplate("mails/admin/newRequirement.html");

  @Override
  public Email getAdminNewRequirementMail(Requirement requirement) {
    String subject = requirement.getCompany().getName() + " - Nouveau besoin";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("requirement", requirement);
    context.put("company", requirement.getCompany());
    Writer writer = new StringWriter();
    try {
      adminNewRequirementMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple(SALES_TEAM_EMAILS)
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
        .toMultiple(SALES_TEAM_EMAILS)
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
        .toMultiple(SALES_TEAM_EMAILS)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentActivationMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentActivation.html");

  @Override
  public Email getAdminTalentActivationMail(Talent talent) {
    String subject = talent.getLastName() + " s'est réactivé";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    Writer writer = new StringWriter();
    try {
      adminTalentActivationMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple(SALES_TEAM_EMAILS)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentDeactivationMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentDeactivation.html");

  @Override
  public Email getAdminTalentDeactivationMail(Talent talent, String deactivationReason,
      List<String> companyNames) {
    String subject = talent.getLastName() + " s'est désactivé";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    context.put("deactivationReason", deactivationReason);
    context.put("companyNames", companyNames);
    Writer writer = new StringWriter();
    try {
      adminTalentDeactivationMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .toMultiple(SALES_TEAM_EMAILS)
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
        .toMultiple(SALES_TEAM_EMAILS)
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
        .toMultiple(SALES_TEAM_EMAILS)
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
        .toMultiple(SALES_TEAM_EMAILS)
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
        .toMultiple(SALES_TEAM_EMAILS)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentPendingOpportunitiesFollowUpLinkedInMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentPendingOpportunitiesFollowUpLinkedIn.html");

  @Override
  public Email getAdminTalentPendingOpportunitiesFollowUpLinkedInMail(
      Collection<String[]> talents) {
    String subject = "[Pending opportunities follow-up] LinkedIn - " + LocalDate.now().toString();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talents", talents);
    Writer writer = new StringWriter();
    try {
      adminTalentPendingOpportunitiesFollowUpLinkedInMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .to(CEO_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentPendingOpportunitiesFollowUpSMSMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentPendingOpportunitiesFollowUpSMS.html");

  @Override
  public Email getAdminTalentPendingOpportunitiesFollowUpSMSMail(Collection<String[]> talents) {
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
        .to(CEO_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate adminTalentPendingOpportunitiesFollowUpCallMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentPendingOpportunitiesFollowUpCall.html");

  @Override
  public Email getAdminTalentPendingOpportunitiesFollowUpCallMail(Collection<String[]> talents) {
    String subject = "[Pending opportunities follow-up] Call - " + LocalDate.now().toString();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talents", talents);
    Writer writer = new StringWriter();
    try {
      adminTalentPendingOpportunitiesFollowUpCallMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .to("bruno@deepfish.co")
        .cc(CEO_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }
}
