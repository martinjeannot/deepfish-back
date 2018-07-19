package com.deepfish.batch.jobs;

import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.CALLING_FOLLOW_UP_STEP_NAME;
import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.LINKED_IN_FOLLOW_UP_STEP_NAME;
import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.SMSING_FOLLOW_UP_STEP_NAME;

import com.deepfish.batch.UnboundUriBuilder;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.simplejavamail.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

public class AdminPendingOpportunitiesFollowUpItemProcessor implements
    ItemProcessor<Opportunity, Opportunity> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AdminPendingOpportunitiesFollowUpItemProcessor.class);

  private final UnboundUriBuilder uriBuilder;

  private final MailFactory mailFactory;

  private final MailService mailService;

  private final Map<UUID, String[]> talentMap = new HashMap<>();

  private StepExecution stepExecution;

  public AdminPendingOpportunitiesFollowUpItemProcessor(UnboundUriBuilder uriBuilder,
      MailFactory mailFactory, MailService mailService) {
    this.uriBuilder = uriBuilder;
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Opportunity process(Opportunity item) throws Exception {
    UUID talentId = item.getTalent().getId();
    if (!talentMap.containsKey(talentId)) {
      switch (stepExecution.getStepName()) {
        case LINKED_IN_FOLLOW_UP_STEP_NAME:
        case SMSING_FOLLOW_UP_STEP_NAME:
          talentMap.put(talentId,
              new String[]{item.getTalent().getFirstName() + " " + item.getTalent().getLastName(),
                  uriBuilder.getTalentDataManagementUri(talentId)});
          break;
        case CALLING_FOLLOW_UP_STEP_NAME:
          talentMap.put(talentId,
              new String[]{item.getTalent().getFirstName() + " " + item.getTalent().getLastName(),
                  uriBuilder.getTalentDataManagementUri(talentId),
                  formatPhoneNumber(item.getTalent().getPhoneNumber())});
          break;
        default:
          throw new IllegalArgumentException("Unknown step : " + stepExecution.getStepName());
      }
    }
    return item;
  }

  @BeforeStep
  void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }

  @AfterStep
  ExitStatus afterStep(StepExecution stepExecution) {
    if (!talentMap.isEmpty()) {
      Email followUpMail;
      switch (stepExecution.getStepName()) {
        case LINKED_IN_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory
              .getAdminTalentPendingOpportunitiesFollowUpLinkedInMail(talentMap.values());
          break;
        case SMSING_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory
              .getAdminTalentPendingOpportunitiesFollowUpSMSMail(talentMap.values());
          break;
        case CALLING_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory
              .getAdminTalentPendingOpportunitiesFollowUpCallMail(talentMap.values());
          break;
        default:
          throw new IllegalArgumentException("Unknown step : " + stepExecution.getStepName());
      }
      mailService.send(followUpMail);
    }
    return ExitStatus.COMPLETED;
  }

  private String formatPhoneNumber(String phoneNumber) {
    switch (phoneNumber.length()) {
      case 10:
        return phoneNumber.replaceAll("(.{2})", "$1 ");
      default:
        return phoneNumber;
    }
  }
}
