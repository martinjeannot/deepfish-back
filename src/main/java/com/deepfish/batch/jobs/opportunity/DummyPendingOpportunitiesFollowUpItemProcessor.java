package com.deepfish.batch.jobs.opportunity;

import com.deepfish.talent.domain.opportunity.Opportunity;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

/**
 * This class only use is to populate the already addressed talents collection. We need this
 * processing for "time-filling" purposes otherwise the most recent opportunity sent might not be
 * taken into account
 */
public class DummyPendingOpportunitiesFollowUpItemProcessor extends
    AbstractPendingOpportunitiesFollowUpItemProcessor {

  @Override
  public Opportunity process(Opportunity item) throws Exception {
    getAlreadyAddressedTalentIds().add(item.getTalent().getId());
    return item;
  }

  @BeforeStep
  protected void beforeStep(StepExecution stepExecution) {
    super.beforeStep(stepExecution);
  }

  @AfterStep
  protected ExitStatus afterStep(StepExecution stepExecution) {
    return super.afterStep(stepExecution);
  }
}
