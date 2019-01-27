package com.deepfish.interview.services;

import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.domain.InterviewStatus;
import com.deepfish.interview.domain.ParticipationStatus;
import com.deepfish.interview.repositories.InterviewRepository;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import java.util.Collection;
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

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultInterviewService(
      InterviewRepository interviewRepository,
      MailService mailService,
      MailFactory mailFactory
  ) {
    this.interviewRepository = interviewRepository;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public Iterable<Interview> scheduleInterviews(Iterable<Interview> interviews) {
    Interview referenceInterview = interviews.iterator().next();
    UUID newSharedId = Objects.isNull(referenceInterview.getSharedId()) ? UUID.randomUUID() : null;

    interviews.forEach(interview -> {
      // currently only employers can schedule interviews but this may change in the future
      interview.handleEmployerResponse(ParticipationStatus.ACCEPTED);
      if (Objects.isNull(interview.getSharedId())) {
        // add shared id
        interview.setSharedId(newSharedId);
      }
    });

    interviews = interviewRepository.save(interviews);

    // send notifications
    mailService.send(mailFactory.getTalentInterviewRequestMail(interviews));
    mailService.send(mailFactory.getAdminNewInterviewRequestMail(interviews.iterator().next()));

    return interviews;
  }

  @Override
  public void updateInterviewStatus(Interview interview) {
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
          // mailService.send(mailFactory.getTalentInterviewConfirmedMail(interview));
          mailService.send(mailFactory.getAdminInterviewConfirmedMail(interview));
          break;
        default:
          return;
      }
    }
  }
}
