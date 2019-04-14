package com.deepfish.talent.services;

import static com.deepfish.talent.domain.opportunity.OpportunityStatus.DECLINED;
import static com.deepfish.talent.domain.opportunity.OpportunityStatus.PENDING;

import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.domain.InterviewStatus;
import com.deepfish.interview.domain.ParticipationStatus;
import com.deepfish.interview.services.InterviewService;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.QOpportunity;
import com.deepfish.talent.repositories.OpportunityRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DefaultOpportunityService implements OpportunityService {

  private final MailService mailService;

  private final MailFactory mailFactory;

  private final InterviewService interviewService;

  private final OpportunityRepository opportunityRepository;

  public DefaultOpportunityService(
      MailService mailService,
      MailFactory mailFactory,
      InterviewService interviewService,
      OpportunityRepository opportunityRepository
  ) {
    this.mailService = mailService;
    this.mailFactory = mailFactory;
    this.interviewService = interviewService;
    this.opportunityRepository = opportunityRepository;
  }

  @Override
  public List<String> declineInBulk(UUID talentId, String bulkDeclinationReason) {
    List<String> companyNames = new ArrayList<>();
    // the number of pending opportunities per talent should not justify a batch update here
    QOpportunity opportunity = QOpportunity.opportunity;
    BooleanExpression fromTalent = opportunity.talent.id.eq(talentId);
    BooleanExpression isPending = opportunity.talentStatus.eq(PENDING);
    opportunityRepository.findAll(fromTalent.and(isPending)).forEach(pendingOpportunity -> {
      pendingOpportunity.handleTalentResponse(DECLINED, bulkDeclinationReason, true);
      opportunityRepository.save(pendingOpportunity);
      companyNames.add(pendingOpportunity.getRequirement().getCompany().getName());
    });
    return companyNames;
  }

  @Override
  public void handleEmployerDeclination(Opportunity declinedOpportunity) {
    if (!DECLINED.equals(declinedOpportunity.getEmployerStatus())) {
      throw new IllegalArgumentException(
          "Opportunity " + declinedOpportunity.getId() + " is not declined employer side");
    }
    switch (declinedOpportunity.getPreviousEmployerStatus()) {
      case PENDING:
        mailService.send(mailFactory.getAdminEmployerDeclinedTalentMail(declinedOpportunity));
        break;
      case ACCEPTED:
        mailService.send(mailFactory.getAdminEmployerDisqualifiedTalentMail(declinedOpportunity));
        break;
      default:
        throw new IllegalStateException("Opportunity " + declinedOpportunity.getId()
            + " previous employer status should be PENDING or ACCEPTED : " + declinedOpportunity
            .getPreviousEmployerStatus());
    }
    List<Interview> declinedInterviews = declinedOpportunity
        .getInterviews()
        .stream()
        .filter(interview -> InterviewStatus.TENTATIVE.equals(interview.getStatus()))
        .map(interview -> interview.handleEmployerResponse(ParticipationStatus.DECLINED))
        .collect(Collectors.toList());
    interviewService.handleEmployerDeclination(declinedInterviews);
  }
}
