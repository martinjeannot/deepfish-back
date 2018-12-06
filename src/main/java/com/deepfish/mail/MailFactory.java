package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.Collection;
import java.util.List;
import org.simplejavamail.email.Email;

public interface MailFactory {

  // TALENT ========================================================================================

  Email getTalentWelcomeMail(Talent talent);

  Email getTalentNewOpportunityMail(Opportunity opportunity);

  Email getTalentPendingOpportunityFollowUp2ndMail(Opportunity opportunity);

  Email getTalentAcceptedByEmployerMail(Opportunity opportunity);

  Email getTalentIncompleteProfile1stMail(Talent talent);

  // EMPLOYER ======================================================================================

  Email getEmployerWelcomeMail(Employer employer, String password);

  Email getEmployerWelcomeFromTypeformMail(Employer employer, String password);

  Email getEmployerPasswordResetMail(Employer employer, String password);

  // ADMIN =========================================================================================

  Email getAdminNewEmployerMail(Employer employer);

  Email getAdminNewRequirementMail(Requirement requirement);

  Email getAdminTalentAcceptedOpportunityMail(Opportunity opportunity);

  Email getAdminTalentDeclinedOpportunityMail(Opportunity opportunity);

  Email getAdminTalentActivationMail(Talent talent);

  Email getAdminTalentDeactivationMail(Talent talent, String declinationReason,
      List<String> companyNames);

  Email getAdminEmployerAcceptedTalentMail(Opportunity opportunity);

  Email getAdminEmployerDeclinedTalentMail(Opportunity opportunity);

  Email getAdminEmployerDisqualifiedTalentMail(Opportunity opportunity);

  Email getAdminEmployerRequestMail(Employer employer, Talent talent, String message);

  Email getAdminTalentPendingOpportunitiesFollowUpLinkedInMail(Collection<String[]> talents);

  Email getAdminTalentPendingOpportunitiesFollowUpSMSMail(Collection<String[]> talents);

  Email getAdminTalentPendingOpportunitiesFollowUpCallMail(Collection<String[]> talents);
}
