package com.deepfish.batch.jobs;

import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.SECOND_MAILING_FOLLOW_UP_STEP_NAME;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.simplejavamail.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

public class TalentPendingOpportunitiesFollowUpItemProcessor implements
    ItemProcessor<Opportunity, Opportunity> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TalentPendingOpportunitiesFollowUpItemProcessor.class);

  private final MailFactory mailFactory;

  private final MailService mailService;

  private final List<UUID> talentIds = new ArrayList<>();

  private StepExecution stepExecution;

  public TalentPendingOpportunitiesFollowUpItemProcessor(MailFactory mailFactory,
      MailService mailService) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Opportunity process(Opportunity item) throws Exception {
    UUID talentId = item.getTalent().getId();
    if (!talentIds.contains(talentId)) {
      talentIds.add(talentId);
      Email followUpMail;
      switch (stepExecution.getStepName()) {
        case SECOND_MAILING_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory.getTalentPendingOpportunityFollowUp2ndMail(item);
          break;
        default:
          throw new IllegalArgumentException("Unknown step : " + stepExecution.getStepName());
      }
      mailService.send(followUpMail);
    }
    return item;
  }

  @BeforeStep
  void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }
}
