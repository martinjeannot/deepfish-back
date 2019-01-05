package com.deepfish.interview.event;

import com.deepfish.interview.domain.Interview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class InterviewEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(InterviewEventHandler.class);

  @HandleBeforeCreate
  public void onBeforeCreate(Interview interview) {
    LOGGER.info("ON BEFORE CREATE");
  }

  @HandleAfterCreate
  public void onAfterCreate(Interview interview) {
    LOGGER.info("ON AFTER CREATE");
  }
}
