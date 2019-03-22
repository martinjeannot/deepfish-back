package com.deepfish.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"staging", "dev", "local"})
public class NoOpLinkedinProfileScrapingTasklet implements LinkedinProfileScrapingTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    return RepeatStatus.FINISHED;
  }
}
