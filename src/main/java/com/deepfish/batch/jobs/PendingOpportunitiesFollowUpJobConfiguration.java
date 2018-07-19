package com.deepfish.batch.jobs;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.UnboundUriBuilder;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.repositories.OpportunityRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class PendingOpportunitiesFollowUpJobConfiguration {

  public static final String JOB_NAME = "PENDING_OPPORTUNITIES_FOLLOW_UP_JOB";

  public static final String FIRST_MAILING_FOLLOW_UP_STEP_NAME = "FIRST_MAILING_FOLLOW_UP_STEP";

  public static final String LINKED_IN_FOLLOW_UP_STEP_NAME = "LINKED_IN_FOLLOW_UP_STEP";

  public static final String SMSING_FOLLOW_UP_STEP_NAME = "SMSING_FOLLOW_UP_STEP";

  public static final String CALLING_FOLLOW_UP_STEP_NAME = "CALLING_FOLLOW_UP_STEP";

  public static final String SECOND_MAILING_FOLLOW_UP_STEP_NAME = "SECOND_MAILING_FOLLOW_UP_STEP";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public PendingOpportunitiesFollowUpJobConfiguration(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // PENDING OPPORTUNITIES FOLLOW UP JOB ===========================================================

  @Bean
  public Job pendingOpportunitiesFollowUpJob(
      Step authenticationStep,
      Step firstMailingFollowUpStep,
      Step linkedInFollowUpStep,
      Step smsingFollowUpStep,
      Step callingFollowUpStep,
      Step secondMailingFollowUpStep,
      Step clearAuthenticationStep) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(firstMailingFollowUpStep)
        .next(linkedInFollowUpStep)
        .next(smsingFollowUpStep)
        .next(callingFollowUpStep)
        .next(secondMailingFollowUpStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // FIRST MAILING FOLLOW UP STEP  =================================================================

  @JobScope
  @Bean
  public Step firstMailingFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesFor1stMailingFollowUpItemReader,
      MailFactory mailFactory, MailService mailService) {
    return stepBuilderFactory
        .get(FIRST_MAILING_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesFor1stMailingFollowUpItemReader)
        .processor(new TalentPendingOpportunitiesFollowUpItemProcessor(mailFactory, mailService))
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesFor1stMailingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(2).atTime(12, 0), LocalDate.now().minusDays(1).atTime(12, 0));
  }

  // LINKED IN FOLLOW UP STEP ======================================================================

  @JobScope
  @Bean
  public Step linkedInFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesForLinkedInFollowUpItemReader,
      UnboundUriBuilder uriBuilder, MailFactory mailFactory, MailService mailService) {
    return stepBuilderFactory
        .get(LINKED_IN_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesForLinkedInFollowUpItemReader)
        .processor(new AdminPendingOpportunitiesFollowUpItemProcessor(uriBuilder, mailFactory,
            mailService))
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForLinkedInFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(3).atTime(12, 0), LocalDate.now().minusDays(2).atTime(12, 0));
  }

  // SMSING FOLLOW UP STEP =========================================================================

  @JobScope
  @Bean
  public Step smsingFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesForSMSingFollowUpItemReader,
      UnboundUriBuilder uriBuilder, MailFactory mailFactory, MailService mailService) {
    return stepBuilderFactory
        .get(SMSING_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesForSMSingFollowUpItemReader)
        .processor(new AdminPendingOpportunitiesFollowUpItemProcessor(uriBuilder, mailFactory,
            mailService))
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForSMSingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(5).atTime(12, 0), LocalDate.now().minusDays(4).atTime(12, 0));
  }

  // CALLING FOLLOW UP STEP ========================================================================

  @JobScope
  @Bean
  public Step callingFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesForCallingFollowUpItemReader,
      UnboundUriBuilder uriBuilder, MailFactory mailFactory, MailService mailService) {
    return stepBuilderFactory
        .get(CALLING_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesForCallingFollowUpItemReader)
        .processor(new AdminPendingOpportunitiesFollowUpItemProcessor(uriBuilder, mailFactory,
            mailService))
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForCallingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(7).atTime(12, 0), LocalDate.now().minusDays(6).atTime(12, 0));
  }

  // SECOND MAILING FOLLOW UP STEP =================================================================

  @JobScope
  @Bean
  public Step secondMailingFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesFor2ndMailingFollowUpItemReader,
      MailFactory mailFactory, MailService mailService) {
    return stepBuilderFactory
        .get(SECOND_MAILING_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesFor2ndMailingFollowUpItemReader)
        .processor(new TalentPendingOpportunitiesFollowUpItemProcessor(mailFactory, mailService))
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesFor2ndMailingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(9).atTime(12, 0), LocalDate.now().minusDays(8).atTime(12, 0));
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean opportunityFollowUpJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean opportunityFollowUpJobTriggerFactoryBean() {
    return BatchConfiguration
        //.getCronTriggerFactoryBean(opportunityFollowUpJobDetailFactoryBean(), "0 0/1 * 1/1 * ? *");
        .getCronTriggerFactoryBean(opportunityFollowUpJobDetailFactoryBean(), "0 0 7 1/1 * ? *");
  }

  // UTILS =========================================================================================

  private ItemReader<Opportunity> getPendingOpportunitiesItemReader(
      OpportunityRepository opportunityRepository, LocalDateTime createdAtAfter,
      LocalDateTime createdAtBefore) {
    RepositoryItemReader<Opportunity> reader = new RepositoryItemReader<>();
    reader.setRepository(opportunityRepository);
    reader.setMethodName("findByTalentStatusAndCreatedAtBetween");
    reader.setArguments(Arrays.asList(OpportunityStatus.PENDING, createdAtAfter, createdAtBefore));
    reader.setPageSize(100);
    Map<String, Direction> sorts = new HashMap<>();
    sorts.put("createdAt", Direction.ASC);
    reader.setSort(sorts);
    return reader;
  }
}
