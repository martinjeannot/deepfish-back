package com.deepfish.mail;

import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import org.simplejavamail.email.Email;

public interface MailFactory {

  // TALENT ========================================================================================

  Email getTalentWelcomeMail(Talent talent);

  Email getTalentNewOpportunityMail(Opportunity opportunity);

  Email getTalentAcceptedOpportunityMail(Opportunity opportunity);

  // EMPLOYER ======================================================================================

  Email getEmployerWelcomeMail(Employer employer, String password);

  Email getEmployerPasswordResetMail(Employer employer, String password);

  // ADMIN =========================================================================================

  Email getAdminNewEmployerMail(Employer employer);

  Email getAdminTalentAcceptedOpportunityMail(Opportunity opportunity);

  Email getAdminTalentDeclinedOpportunityMail(Opportunity opportunity);

  Email getAdminEmployerAcceptedTalentMail(Opportunity opportunity);

  Email getAdminEmployerDeclinedTalentMail(Opportunity opportunity);
}
