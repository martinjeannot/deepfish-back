package com.deepfish.batch.jobs;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.simplejavamail.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class OpportunityPendingFor24hItemProcessor implements
    ItemProcessor<Opportunity, Opportunity> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(OpportunityPendingFor24hItemProcessor.class);

  private final MailFactory mailFactory;

  private final MailService mailService;

  private final List<UUID> talentIds = new ArrayList<>();

  public OpportunityPendingFor24hItemProcessor(MailFactory mailFactory, MailService mailService) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Opportunity process(Opportunity item) throws Exception {
    UUID talentId = item.getTalent().getId();
    if (!talentIds.contains(talentId)) {
      Email followUpMail = mailFactory.getTalentOpportunityPendingFor24hMail(item);
      mailService.send(followUpMail);
      talentIds.add(talentId);
    }
    return item;
  }
}
