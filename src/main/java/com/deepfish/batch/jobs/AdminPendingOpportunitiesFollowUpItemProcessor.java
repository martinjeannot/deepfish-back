package com.deepfish.batch.jobs;

import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.CALLING_FOLLOW_UP_STEP_NAME;
import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.LINKED_IN_FOLLOW_UP_STEP_NAME;
import static com.deepfish.batch.jobs.PendingOpportunitiesFollowUpJobConfiguration.SMSING_FOLLOW_UP_STEP_NAME;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.mail.util.UnboundUriBuilder;
import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.simplejavamail.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class AdminPendingOpportunitiesFollowUpItemProcessor extends
    AbstractPendingOpportunitiesFollowUpItemProcessor {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AdminPendingOpportunitiesFollowUpItemProcessor.class);

  private final UnboundUriBuilder uriBuilder;

  private final MailFactory mailFactory;

  private final MailService mailService;

  private final List<String[]> talents = new ArrayList<>();

  public AdminPendingOpportunitiesFollowUpItemProcessor(UnboundUriBuilder uriBuilder,
      MailFactory mailFactory, MailService mailService) {
    this.uriBuilder = uriBuilder;
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Opportunity process(Opportunity item) throws Exception {
    UUID talentId = item.getTalent().getId();
    if (!getAlreadyAddressedTalentIds().contains(talentId)) {
      getAlreadyAddressedTalentIds().add(talentId);
      switch (getStepExecution().getStepName()) {
        case LINKED_IN_FOLLOW_UP_STEP_NAME:
        case SMSING_FOLLOW_UP_STEP_NAME:
          talents.add(
              new String[]{item.getTalent().getFirstName() + " " + item.getTalent().getLastName(),
                  uriBuilder.getTalentDataManagementUri(talentId)});
          break;
        case CALLING_FOLLOW_UP_STEP_NAME:
          talents.add(
              new String[]{item.getTalent().getFirstName() + " " + item.getTalent().getLastName(),
                  uriBuilder.getTalentDataManagementUri(talentId),
                  formatPhoneNumber(item.getTalent().getPhoneNumber())});
          break;
        default:
          throw new IllegalArgumentException("Unknown step : " + getStepExecution().getStepName());
      }
    }
    return item;
  }

  @BeforeStep
  protected void beforeStep(StepExecution stepExecution) {
    super.beforeStep(stepExecution);
  }

  @AfterStep
  protected ExitStatus afterStep(StepExecution stepExecution) {
    if (!talents.isEmpty()) {
      Email followUpMail;
      switch (stepExecution.getStepName()) {
        case LINKED_IN_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory
              .getAdminTalentPendingOpportunitiesFollowUpLinkedInMail(talents);
          break;
        case SMSING_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory.getAdminTalentPendingOpportunitiesFollowUpSMSMail(talents);
          break;
        case CALLING_FOLLOW_UP_STEP_NAME:
          followUpMail = mailFactory.getAdminTalentPendingOpportunitiesFollowUpCallMail(talents);
          break;
        default:
          throw new IllegalArgumentException("Unknown step : " + stepExecution.getStepName());
      }
      // TODO admin mails deactivated upon request of sales team, MDEA process remains unknown/unclearly defined
      // mailService.send(followUpMail);
    }
    return super.afterStep(stepExecution);
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
