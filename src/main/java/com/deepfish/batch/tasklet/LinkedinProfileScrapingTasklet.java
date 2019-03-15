package com.deepfish.batch.tasklet;

import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class LinkedinProfileScrapingTasklet implements Tasklet {

  private final TalentRepository talentRepository;

  public LinkedinProfileScrapingTasklet(
      TalentRepository talentRepository
  ) {
    this.talentRepository = talentRepository;
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    List<Talent> talents = talentRepository
        .findFirst20ByLinkedinProfileLastRetrievalAttemptedAtIsNullAndLinkedinPublicProfileUrlIsNotNullOrderByCreatedAtDesc();

    talents.forEach(talent -> talent
        .setLinkedinProfileLastRetrievalAttemptedAt(LocalDateTime.now(Clock.systemUTC())));
    // talentRepository.save(talents);

    return RepeatStatus.FINISHED;
  }
}
