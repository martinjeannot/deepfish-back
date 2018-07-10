package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.Collection;
import org.simplejavamail.email.Email;

public interface MailFactory {

  // TALENT ========================================================================================

  Email getTalentWelcomeMail(Talent talent);

  Email getTalentNewOpportunityMail(Opportunity opportunity);

  Email getTalentAcceptedOpportunityMail(Opportunity opportunity);

  Email getTalentPendingOpportunityFollowUp1stMail(Opportunity opportunity);

  Email getTalentPendingOpportunityFollowUp2ndMail(Opportunity opportunity);

  // EMPLOYER ======================================================================================

  Email getEmployerWelcomeMail(Employer employer, String password);

  Email getEmployerPasswordResetMail(Employer employer, String password);

  // ADMIN =========================================================================================

  Email getAdminNewEmployerMail(Employer employer);

  Email getAdminTalentAcceptedOpportunityMail(Opportunity opportunity);

  Email getAdminTalentDeclinedOpportunityMail(Opportunity opportunity);

  Email getAdminEmployerAcceptedTalentMail(Opportunity opportunity);

  Email getAdminEmployerDeclinedTalentMail(Opportunity opportunity);

  Email getAdminEmployerDisqualifiedTalentMail(Opportunity opportunity);

  Email getAdminEmployerRequestMail(Employer employer, Talent talent, String message);

  Email getAdminTalentPendingOpportunitiesFollowUpLinkedInMail(Collection<String> talents);

  Email getAdminTalentPendingOpportunitiesFollowUpSMSMail(Collection<String> talents);

  Email getAdminTalentPendingOpportunitiesFollowUpCallMail(Collection<String> talents);
}
