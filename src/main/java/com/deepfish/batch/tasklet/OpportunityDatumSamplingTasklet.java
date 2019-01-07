package com.deepfish.batch.tasklet;

import com.deepfish.talent.domain.opportunity.OpportunityDatum;
import com.deepfish.talent.repositories.OpportunityDatumRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class OpportunityDatumSamplingTasklet implements Tasklet {

  private final OpportunityDatumRepository opportunityDatumRepository;

  public OpportunityDatumSamplingTasklet(
      OpportunityDatumRepository opportunityDatumRepository
  ) {
    this.opportunityDatumRepository = opportunityDatumRepository;
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    OpportunityDatum current = opportunityDatumRepository.findCurrent();
    current.calculateVariations(opportunityDatumRepository.findFirstByOrderByCreatedAtDesc());
    opportunityDatumRepository.save(current);

    return RepeatStatus.FINISHED;
  }
}
