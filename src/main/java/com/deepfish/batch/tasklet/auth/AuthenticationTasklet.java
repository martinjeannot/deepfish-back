package com.deepfish.batch.tasklet.auth;

import com.deepfish.security.SystemAuthentication;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    SecurityContextHolder.getContext().setAuthentication(SystemAuthentication.getAuthentication());

    return RepeatStatus.FINISHED;
  }
}
