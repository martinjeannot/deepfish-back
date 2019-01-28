package com.deepfish.interview.event;

import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.services.InterviewService;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class InterviewEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(InterviewEventHandler.class);

  private final InterviewService interviewService;

  public InterviewEventHandler(
      InterviewService interviewService
  ) {
    this.interviewService = interviewService;
  }

  @HandleBeforeSave
  public void onBeforeSave(Interview interview) {
    if (Objects.nonNull(interview.getPreviousState())) {
      interview.handleTalentResponseFromPreviousState();
    }
  }

  @HandleAfterSave
  public void onAfterSave(Interview interview) {
    interviewService.updateInterviewStatus(interview);
  }
}
