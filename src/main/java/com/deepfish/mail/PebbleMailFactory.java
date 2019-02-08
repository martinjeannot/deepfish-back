package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.interview.domain.Interview;
import com.deepfish.mail.util.FrontAppUrlBuilder;
import com.deepfish.mail.util.MailHelper;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;

@Component
public class PebbleMailFactory implements MailFactory {

  private static final String DAVID_EMAIL = "david@deepfish.co";

  private static final String[] SALES_TEAM_EMAILS = new String[]{
      DAVID_EMAIL,
      "bruno@deepfish.co",
  };

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
      .ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.FRANCE);

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
      .ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.FRANCE);

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
        .from(DAVID_EMAIL)
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
        .from(DAVID_EMAIL)
        .to(opportunity.getTalent().getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate talent1stIncompleteProfileMailTemplate = pebbleEngine
      .getTemplate("mails/talent/incompleteProfile1.html");

  @Override
  public Email getTalent1stIncompleteProfileMail(Talent talent) {
    String subject = talent.getFirstName() + ", tu y es presque - Deepfish";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    Writer writer = new StringWriter();
    try {
      talent1stIncompleteProfileMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(talent.getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate talent2ndIncompleteProfileMailTemplate = pebbleEngine
      .getTemplate("mails/talent/incompleteProfile2.html");

  @Override
  public Email getTalent2ndIncompleteProfileMail(Talent talent) {
    String subject = talent.getFirstName() + ", ton profil Deepfish est toujours incomplet";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    Writer writer = new StringWriter();
    try {
      talent2ndIncompleteProfileMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(talent.getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate talent3rdIncompleteProfileMailTemplate = pebbleEngine
      .getTemplate("mails/talent/incompleteProfile3.html");

  @Override
  public Email getTalent3rdIncompleteProfileMail(Talent talent) {
    String subject = talent.getFirstName() + ", last call - Deepfish";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    Writer writer = new StringWriter();
    try {
      talent3rdIncompleteProfileMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(talent.getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate talent4thIncompleteProfileMailTemplate = pebbleEngine
      .getTemplate("mails/talent/incompleteProfile4.html");

  @Override
  public Email getTalent4thIncompleteProfileMail(Talent talent) {
    String subject = talent.getFirstName() + ", ton profil Deepfish est désactivé";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    Writer writer = new StringWriter();
    try {
      talent4thIncompleteProfileMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(talent.getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate talentInterviewRequestMailTemplate = pebbleEngine
      .getTemplate("mails/talent/interviewRequest.html");

  @Override
  public Email getTalentInterviewRequestMail(Iterable<Interview> interviews) {
    Interview referenceInterview = interviews.iterator().next();
    Talent talent = referenceInterview.getTalent();
    String subject = talent.getFirstName() + ", un recruteur veut te rencontrer !";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    context.put("company", referenceInterview.getEmployer().getCompany());
    context.put("format", MailHelper.getLabelForInterviewFormat(referenceInterview.getFormat()));
    context.put("interviews", interviews);
    context.put("dateFormatter", DATE_FORMATTER);
    context.put("timeFormatter", TIME_FORMATTER);
    Writer writer = new StringWriter();
    try {
      talentInterviewRequestMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(talent.getEmail())
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }

  private final PebbleTemplate talentInterviewConfirmedMailTemplate = pebbleEngine
      .getTemplate("mails/talent/interviewConfirmed.html");

  @Override
  public Email getTalentInterviewConfirmedMail(Interview interview) {
    String subject = interview.getTalent().getFirstName()
        + ", ton entretien avec "
        + interview.getEmployer().getCompany().getName()
        + " est confirmé !";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", interview.getTalent());
    context.put("format", MailHelper.getLabelForInterviewFormat(interview.getFormat()));
    context.put("company", interview.getEmployer().getCompany());
    context.put("interview", interview);
    context.put("dateFormatter", DATE_FORMATTER);
    context.put("timeFormatter", TIME_FORMATTER);
    Writer writer = new StringWriter();
    try {
      talentInterviewConfirmedMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(interview.getTalent().getEmail())
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
        .from(DAVID_EMAIL)
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
        .from(DAVID_EMAIL)
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

  private final PebbleTemplate employerInterviewConfirmedMailTemplate = pebbleEngine
      .getTemplate("mails/employer/interviewConfirmed.html");

  @Override
  public Email getEmployerInterviewConfirmedMail(Interview interview) {
    String subject = "Votre entretien avec "
        + interview.getTalent().getFirstName() + " "
        + interview.getTalent().getLastName()
        + " est confirmé via Deepfish";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("employer", interview.getEmployer());
    context.put("format", MailHelper.getLabelForInterviewFormat(interview.getFormat()));
    context.put("talent", interview.getTalent());
    context.put("interview", interview);
    context.put("dateFormatter", DATE_FORMATTER);
    context.put("timeFormatter", TIME_FORMATTER);
    Writer writer = new StringWriter();
    try {
      employerInterviewConfirmedMailTemplate.evaluate(writer, context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return EmailBuilder
        .startingBlank()
        .from(DAVID_EMAIL)
        .to(interview.getEmployer().getUsername())
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

  private final PebbleTemplate adminNewInterviewRequestMailTemplate = pebbleEngine
      .getTemplate("mails/admin/newInterviewRequest.html");

  @Override
  public Email getAdminNewInterviewRequestMail(Interview interview) {
    String subject = "[Interview request] " + interview.getEmployer().getCompany().getName() + " - "
        + interview.getTalent().getFirstName() + " "
        + interview.getTalent().getLastName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("company", interview.getEmployer().getCompany());
    context.put("talent", interview.getTalent());
    Writer writer = new StringWriter();
    try {
      adminNewInterviewRequestMailTemplate.evaluate(writer, context);
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

  private final PebbleTemplate adminInterviewConfirmedMailTemplate = pebbleEngine
      .getTemplate("mails/admin/interviewConfirmed.html");

  @Override
  public Email getAdminInterviewConfirmedMail(Interview interview) {
    String subject = "[Interview confirmed] "
        + interview.getTalent().getFirstName() + " "
        + interview.getTalent().getLastName() + " - "
        + interview.getEmployer().getCompany().getName();
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", interview.getTalent());
    context.put("company", interview.getEmployer().getCompany());
    context
        .put("duration", ChronoUnit.MINUTES.between(interview.getStartAt(), interview.getEndAt()));
    context.put("interview", interview);
    context.put("dateFormatter", DATE_FORMATTER);
    context.put("timeFormatter", TIME_FORMATTER);
    Writer writer = new StringWriter();
    try {
      adminInterviewConfirmedMailTemplate.evaluate(writer, context);
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

  private final PebbleTemplate adminTalentReactivationMailTemplate = pebbleEngine
      .getTemplate("mails/admin/talentReactivation.html");

  @Override
  public Email getAdminTalentReactivationMail(Talent talent) {
    String subject = "[Talent reactivated] " + talent.getFirstName() + " " + talent.getLastName()
        + " has been reactivated";
    Map<String, Object> context = new HashMap<>();
    context.put("title", subject);
    context.put("talent", talent);
    Writer writer = new StringWriter();
    try {
      adminTalentReactivationMailTemplate.evaluate(writer, context);
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
        .to(DAVID_EMAIL)
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
        .to(DAVID_EMAIL)
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
        .cc(DAVID_EMAIL)
        .withSubject(subject)
        .withHTMLText(writer.toString())
        .buildEmail();
  }
}
