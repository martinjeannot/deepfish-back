package com.deepfish.batch.jobs;

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
import org.springframework.batch.item.ItemProcessor;

public class OpportunityPendingFor96hItemProcessor implements
    ItemProcessor<Opportunity, Opportunity> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(OpportunityPendingFor96hItemProcessor.class);

  private final MailFactory mailFactory;

  private final MailService mailService;

  private final Map<UUID, String> talentMap = new HashMap<>();

  public OpportunityPendingFor96hItemProcessor(MailFactory mailFactory, MailService mailService) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Opportunity process(Opportunity item) throws Exception {
    UUID talentId = item.getTalent().getId();
    if (!talentMap.containsKey(talentId)) {
      talentMap.put(talentId,
          item.getTalent().getFirstName() + " " + item.getTalent().getLastName() + " "
              + formatPhoneNumber(item.getTalent().getPhoneNumber()));
    }
    return item;
  }

  @AfterStep
  ExitStatus afterStep(StepExecution stepExecution) {
    Email followUpMail = mailFactory
        .getAdminTalentPendingOpportunitiesFollowUpSMSMail(talentMap.values());
    mailService.send(followUpMail);
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
