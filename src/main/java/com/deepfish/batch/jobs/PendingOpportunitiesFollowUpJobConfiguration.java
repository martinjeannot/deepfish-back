package com.deepfish.batch.jobs;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.mail.util.UnboundUriBuilder;
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
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class PendingOpportunitiesFollowUpJobConfiguration {

  static final String JOB_NAME = "PENDING_OPPORTUNITIES_FOLLOW_UP_JOB";

  static final String LINKED_IN_FOLLOW_UP_STEP_NAME = "LINKED_IN_FOLLOW_UP_STEP";

  static final int LINKED_IN_FOLLOW_UP_STEP_DAY = 2;

  static final String SMSING_FOLLOW_UP_STEP_NAME = "SMSING_FOLLOW_UP_STEP";

  static final int SMSING_FOLLOW_UP_STEP_DAY = 4;

  static final String CALLING_FOLLOW_UP_STEP_NAME = "CALLING_FOLLOW_UP_STEP";

  static final int CALLING_FOLLOW_UP_STEP_DAY = 6;

  static final String SECOND_MAILING_FOLLOW_UP_STEP_NAME = "SECOND_MAILING_FOLLOW_UP_STEP";

  static final int SECOND_MAILING_FOLLOW_UP_STEP_DAY = 9;

  static final String ALREADY_ADDRESSED_TALENT_IDS = "ALREADY_ADDRESSED_TALENT_IDS";

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
      Step preLinkedInFollowUpStep,
      Step linkedInFollowUpStep,
      Step preSMSingFollowUpStep,
      Step smsingFollowUpStep,
      Step preCallingFollowUpStep,
      Step callingFollowUpStep,
      Step preSecondMailingFollowUpStep,
      Step secondMailingFollowUpStep,
      Step clearAuthenticationStep) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(preLinkedInFollowUpStep)
        .next(linkedInFollowUpStep)
        .next(preSMSingFollowUpStep)
        .next(smsingFollowUpStep)
        .next(preCallingFollowUpStep)
        .next(callingFollowUpStep)
        .next(preSecondMailingFollowUpStep)
        .next(secondMailingFollowUpStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // PRE LINKED IN FOLLOW UP STEP ==================================================================

  @JobScope
  @Bean
  public Step preLinkedInFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesForPreLinkedInFollowUpItemReader) {
    return stepBuilderFactory
        .get("PRE_" + LINKED_IN_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesForPreLinkedInFollowUpItemReader)
        .processor(new DummyPendingOpportunitiesFollowUpItemProcessor())
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForPreLinkedInFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(LINKED_IN_FOLLOW_UP_STEP_DAY - 1).atStartOfDay(),
        LocalDateTime.now());
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
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForLinkedInFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(LINKED_IN_FOLLOW_UP_STEP_DAY).atStartOfDay(),
        LocalDate.now().minusDays(LINKED_IN_FOLLOW_UP_STEP_DAY - 1).atStartOfDay());
  }

  // PRE SMSING FOLLOW UP STEP =====================================================================


  @JobScope
  @Bean
  public Step preSMSingFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesForPreSMSingFollowUpItemReader) {
    return stepBuilderFactory
        .get("PRE_" + SMSING_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesForPreSMSingFollowUpItemReader)
        .processor(new DummyPendingOpportunitiesFollowUpItemProcessor())
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForPreSMSingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(SMSING_FOLLOW_UP_STEP_DAY - 1).atStartOfDay(),
        LocalDate.now().minusDays(LINKED_IN_FOLLOW_UP_STEP_DAY).atStartOfDay());
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
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForSMSingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(SMSING_FOLLOW_UP_STEP_DAY).atStartOfDay(),
        LocalDate.now().minusDays(SMSING_FOLLOW_UP_STEP_DAY - 1).atStartOfDay());
  }

  // PRE CALLING FOLLOW UP STEP ====================================================================

  @JobScope
  @Bean
  public Step preCallingFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesForPreCallingFollowUpItemReader) {
    return stepBuilderFactory
        .get("PRE_" + CALLING_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesForPreCallingFollowUpItemReader)
        .processor(new DummyPendingOpportunitiesFollowUpItemProcessor())
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForPreCallingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(CALLING_FOLLOW_UP_STEP_DAY - 1).atStartOfDay(),
        LocalDate.now().minusDays(SMSING_FOLLOW_UP_STEP_DAY).atStartOfDay());
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
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForCallingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(CALLING_FOLLOW_UP_STEP_DAY).atStartOfDay(),
        LocalDate.now().minusDays(CALLING_FOLLOW_UP_STEP_DAY - 1).atStartOfDay());
  }

  // PRE SECOND MAILING FOLLOW UP STEP =============================================================

  @JobScope
  @Bean
  public Step preSecondMailingFollowUpStep(
      ItemReader<Opportunity> pendingOpportunitiesForPre2ndMailingFollowUpItemReader) {
    return stepBuilderFactory
        .get("PRE_" + SECOND_MAILING_FOLLOW_UP_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(pendingOpportunitiesForPre2ndMailingFollowUpItemReader)
        .processor(new DummyPendingOpportunitiesFollowUpItemProcessor())
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesForPre2ndMailingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(SECOND_MAILING_FOLLOW_UP_STEP_DAY - 1).atStartOfDay(),
        LocalDate.now().minusDays(CALLING_FOLLOW_UP_STEP_DAY).atStartOfDay());
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
        .listener(pendingOpportunitiesFollowUpJobExecutionContextPromotionListener())
        .build();
  }

  @JobScope
  @Bean
  public ItemReader<Opportunity> pendingOpportunitiesFor2ndMailingFollowUpItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunitiesItemReader(opportunityRepository,
        LocalDate.now().minusDays(SECOND_MAILING_FOLLOW_UP_STEP_DAY).atStartOfDay(),
        LocalDate.now().minusDays(SECOND_MAILING_FOLLOW_UP_STEP_DAY - 1).atStartOfDay());
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean pendingOpportunitiesFollowUpJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean pendingOpportunitiesFollowUpJobTriggerFactoryBean() {
    return BatchConfiguration
        //.getCronTriggerFactoryBean(pendingOpportunitiesFollowUpJobDetailFactoryBean(),
        //    "0 0/1 * 1/1 * ? *");
        .getCronTriggerFactoryBean(pendingOpportunitiesFollowUpJobDetailFactoryBean(),
            "0 0 7 1/1 * ? *");
  }

  // UTILS =========================================================================================

  @Bean
  public ExecutionContextPromotionListener pendingOpportunitiesFollowUpJobExecutionContextPromotionListener() {
    ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
    executionContextPromotionListener.setKeys(new String[]{ALREADY_ADDRESSED_TALENT_IDS});
    return executionContextPromotionListener;
  }

  private ItemReader<Opportunity> getPendingOpportunitiesItemReader(
      OpportunityRepository opportunityRepository, LocalDateTime createdAtAfter,
      LocalDateTime createdAtBefore) {
    RepositoryItemReader<Opportunity> reader = new RepositoryItemReader<>();
    reader.setRepository(opportunityRepository);
    reader.setMethodName("findByTalentStatusAndCreatedAtBetween");
    reader.setArguments(Arrays.asList(OpportunityStatus.PENDING, createdAtAfter, createdAtBefore));
    reader.setPageSize(100);
    Map<String, Direction> sorts = new HashMap<>();
    sorts.put("createdAt", Direction.DESC);
    reader.setSort(sorts);
    return reader;
  }
}
