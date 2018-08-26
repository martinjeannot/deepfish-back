package com.deepfish.batch.jobs;

import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.ALREADY_ADDRESSED_TALENT_IDS;

import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;

public abstract class AbstractPendingOpportunitiesFollowUpItemProcessor implements
    ItemProcessor<Opportunity, Opportunity> {

  private StepExecution stepExecution;

  /**
   * This collection keeps count of already addressed talents at job level, it is meant to be passed
   * from step to step
   */
  private Set<UUID> alreadyAddressedTalentIds = new HashSet<>();

  protected void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
    // retrieve already addressed talent ids from the previous step
    Object alreadyAddressedTalentIds = stepExecution.getJobExecution().getExecutionContext()
        .get(ALREADY_ADDRESSED_TALENT_IDS);
    if (alreadyAddressedTalentIds != null) {
      this.alreadyAddressedTalentIds = (Set<UUID>) alreadyAddressedTalentIds;
    }
  }

  protected ExitStatus afterStep(StepExecution stepExecution) {
    // pass already addressed talent ids to the next step
    stepExecution.getExecutionContext()
        .put(ALREADY_ADDRESSED_TALENT_IDS, getAlreadyAddressedTalentIds());
    return ExitStatus.COMPLETED;
  }

  StepExecution getStepExecution() {
    return stepExecution;
  }

  Set<UUID> getAlreadyAddressedTalentIds() {
    return alreadyAddressedTalentIds;
  }
}
