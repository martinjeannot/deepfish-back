package com.deepfish.interview.services;

import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.domain.InterviewStatus;
import com.deepfish.interview.domain.ParticipationStatus;
import com.deepfish.interview.repositories.InterviewRepository;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultInterviewService implements InterviewService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInterviewService.class);

  private final InterviewRepository interviewRepository;

  private final EmployerRepository employerRepository;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultInterviewService(
      InterviewRepository interviewRepository,
      EmployerRepository employerRepository,
      MailService mailService,
      MailFactory mailFactory
  ) {
    this.interviewRepository = interviewRepository;
    this.employerRepository = employerRepository;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public Iterable<Interview> scheduleInterviews(Iterable<Interview> interviews) {
    Interview referenceInterview = interviews.iterator().next();
    UUID newSharedId = Objects.isNull(referenceInterview.getSharedId()) ? UUID.randomUUID() : null;

    interviews.forEach(interview -> {
      if (Objects.isNull(interview.getSharedId())) {
        // add shared id
        interview.setSharedId(newSharedId);
      }
      // currently only employers can schedule interviews but this may change in the future
      interview.handleEmployerResponse(ParticipationStatus.ACCEPTED);
    });

    interviews = interviewRepository.save(interviews);

    // send notifications
    mailService.send(mailFactory.getTalentInterviewRequestMail(interviews));
    mailService.send(mailFactory.getAdminNewInterviewRequestMail(interviews));

    return interviews;
  }

  @Override
  public Iterable<Interview> scheduleInterviewsAsAdmin(Iterable<Interview> interviews) {
    Interview referenceInterview = interviews.iterator().next();
    UUID newSharedId = Objects.isNull(referenceInterview.getSharedId()) ? UUID.randomUUID() : null;

    interviews.forEach(interview -> {
      if (Objects.isNull(interview.getSharedId())) {
        // add shared id
        interview.setSharedId(newSharedId);
      }
      // currently only employers can create requirements but this may change in the future
      interview.setEmployer(
          employerRepository.findOne(interview.getOpportunity().getRequirement().getCreatedBy()));
      // handle talent response
      switch (interview.getTalentResponseStatus()) {
        case NEEDS_ACTION:
          break;
        case ACCEPTED:
          interview.handleTalentResponse(ParticipationStatus.ACCEPTED);
          break;
        default:
          throw new IllegalArgumentException(
              "Cannot handle participation status : " + interview.getTalentResponseStatus());
      }
      // handle employer response
      switch (interview.getEmployerResponseStatus()) {
        case ACCEPTED:
          interview.handleEmployerResponse(ParticipationStatus.ACCEPTED);
          break;
        default:
          throw new IllegalArgumentException(
              "Cannot handle participation status : " + interview.getEmployerResponseStatus());
      }
      interview.updateStatus();
    });

    interviews = interviewRepository.save(interviews);

    return interviews;
  }

  @Override
  public void cancelInterview(
      Interview interview,
      String cancelledBy,
      boolean cancelLinkedInterviews
  ) {
    Iterable<Interview> interviews;
    if (cancelLinkedInterviews) {
      interviews = interviewRepository.findBySharedId(interview.getSharedId());
    } else {
      interviews = Collections.singletonList(interview);
    }
    interviews.forEach(interviewToCancel -> {
      switch (cancelledBy) {
        case "TALENT":
          interviewToCancel.handleTalentResponse(ParticipationStatus.DECLINED);
          break;
        case "EMPLOYER":
          interviewToCancel.handleEmployerResponse(ParticipationStatus.DECLINED);
          break;
        default:
          throw new IllegalArgumentException("Unknown cancelledBy : " + cancelledBy);
      }
      interviewToCancel.updateStatus();
    });
    interviewRepository.save(interviews);
  }

  @Override
  public Interview updateInterviewStatus(Interview interview) {
    boolean statusHasBeenUpdated = interview.updateStatus();
    if (statusHasBeenUpdated) {
      switch (interview.getStatus()) {
        case CONFIRMED:
          // cancel related interview requests
          Collection<Interview> interviews = interviewRepository
              .findBySharedId(interview.getSharedId());
          Collection<Interview> interviewsToCancel = interviews.stream()
              .filter(itw -> !itw.equals(interview)).collect(Collectors.toList());
          interviewsToCancel.forEach(interviewToCancel -> {
            // currently only talents can answer interview requests, this may change in the future
            interviewToCancel.handleTalentResponse(ParticipationStatus.DECLINED);
            interviewToCancel.updateStatus();
            if (!InterviewStatus.CANCELLED.equals(interviewToCancel.getStatus())) {
              throw new IllegalStateException("Interview should be cancelled");
            }
          });

          interviewRepository.save(interviews);

          // send notifications
          // TODO replace with google calendar invite
          mailService.send(mailFactory.getEmployerInterviewConfirmedMail(interview));
          mailService.send(mailFactory.getTalentInterviewConfirmedMail(interview));
          mailService.send(mailFactory.getAdminInterviewConfirmedMail(interview));
          break;

        default:
          interviewRepository.save(interview);
          break;
      }
    }
    return interview;
  }

  @Override
  public void handleEmployerDeclination(List<Interview> declinedInterviews) {
    declinedInterviews
        .forEach(Interview::updateStatus);
    interviewRepository.save(declinedInterviews);
  }
}
