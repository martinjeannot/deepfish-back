package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.interview.domain.Interview;
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

  Email getTalent1stIncompleteProfileMail(Talent talent);

  Email getTalent2ndIncompleteProfileMail(Talent talent);

  Email getTalent3rdIncompleteProfileMail(Talent talent);

  Email getTalent4thIncompleteProfileMail(Talent talent);

  Email getTalentInterviewRequestMail(Iterable<Interview> interviews);

  Email getTalentInterviewConfirmedMail(Interview interview);

  Email getTalentQualificationInterviewSchedulingMail(Talent talent);

  // EMPLOYER ======================================================================================

  Email getEmployerWelcomeMail(Employer employer, String password);

  Email getEmployerWelcomeFromTypeformMail(Employer employer, String password);

  Email getEmployerPasswordResetMail(Employer employer, String password);

  Email getEmployerTalentAcceptedOpportunityMail(Opportunity opportunity);

  Email getEmployerInterviewConfirmedMail(Interview interview);

  // ADMIN =========================================================================================

  Email getAdminNewEmployerMail(Employer employer);

  Email getAdminNewRequirementMail(Requirement requirement);

  Email getAdminTalentAcceptedOpportunityMail(Opportunity opportunity);

  Email getAdminTalentDeclinedOpportunityMail(Opportunity opportunity);

  Email getAdminTalentActivationMail(Talent talent);

  Email getAdminTalentReactivationMail(Talent talent);

  Email getAdminTalentDeactivationMail(Talent talent, String declinationReason,
      List<String> companyNames);

  Email getAdminTalentQuestionMail(Opportunity opportunity, String question);

  Email getAdminTalentReferralInvitationMail(Talent talent, String emails, String referralSubject,
      String message);

  Email getAdminEmployerAcceptedTalentMail(Opportunity opportunity);

  Email getAdminEmployerDeclinedTalentMail(Opportunity opportunity);

  Email getAdminEmployerDisqualifiedTalentMail(Opportunity opportunity);

  Email getAdminEmployerRequestMail(Employer employer, Talent talent, String message);

  Email getAdminEmployerFollowUpMail(Employer employer, Talent talent, String message);

  Email getAdminEmployerRequirementUpdateMail(Requirement requirement, String message);

  Email getAdminTalentPendingOpportunitiesFollowUpLinkedInMail(Collection<String[]> talents);

  Email getAdminTalentPendingOpportunitiesFollowUpSMSMail(Collection<String[]> talents);

  Email getAdminTalentPendingOpportunitiesFollowUpCallMail(Collection<String[]> talents);

  Email getAdminNewInterviewRequestMail(Iterable<Interview> interviews);

  Email getAdminInterviewConfirmedMail(Interview interview);

  Email getAdminErrorMail(Exception exception);

  Email getAdminBatchWarningMail(String message);
}
