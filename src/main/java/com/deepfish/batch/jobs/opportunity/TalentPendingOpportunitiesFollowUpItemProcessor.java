package com.deepfish.batch.jobs.opportunity;

import static com.deepfish.batch.jobs.opportunity.PendingOpportunitiesFollowUpJobConfiguration.SECOND_MAILING_FOLLOW_UP_STEP_NAME;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.UUID;
import org.simplejavamail.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class TalentPendingOpportunitiesFollowUpItemProcessor extends
    AbstractPendingOpportunitiesFollowUpItemProcessor {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TalentPendingOpportunitiesFollowUpItemProcessor.class);

  private final MailFactory mailFactory;

  private final MailService mailService;

  public TalentPendingOpportunitiesFollowUpItemProcessor(MailFactory mailFactory,
      MailService mailService) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Opportunity process(Opportunity item) throws Exception {
    UUID talentId = item.getTalent().getId();
    if (!getAlreadyAddressedTalentIds().contains(talentId)) {
      getAlreadyAddressedTalentIds().add(talentId);
      Email followUpMail;
      switch (getStepExecution().getStepName()) {
        case SECOND_MAILING_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory.getTalentPendingOpportunityFollowUp2ndMail(item);
          break;
        default:
          throw new IllegalArgumentException("Unknown step : " + getStepExecution().getStepName());
      }
      mailService.send(followUpMail);
    }
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
