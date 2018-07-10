package com.deepfish.batch.jobs;

import com.deepfish.batch.BatchConfiguration;
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
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class OpportunityFollowUpJobConfiguration {

  private static final String JOB_NAME = "opportunityFollowUpJob";

  private static final String PENDING_FOR_24H_STEP_NAME = "opportunityPendingFor24hStep";

  private static final String PENDING_FOR_48H_STEP_NAME = "opportunityPendingFor48hStep";

  private static final String PENDING_FOR_96H_STEP_NAME = "opportunityPendingFor96hStep";

  private static final String PENDING_FOR_144H_STEP_NAME = "opportunityPendingFor144hStep";

  private static final String PENDING_FOR_192H_STEP_NAME = "opportunityPendingFor192hStep";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public OpportunityFollowUpJobConfiguration(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // OPPORTUNITY FOLLOW UP JOB =====================================================================

  @Bean
  public Job opportunityFollowUpJob(Step authenticationStep, Step opportunityPendingFor24hStep,
      Step opportunityPendingFor48hStep, Step opportunityPendingFor96hStep,
      Step opportunityPendingFor144hStep, Step opportunityPendingFor192hStep,
      Step clearAuthenticationStep) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(opportunityPendingFor24hStep)
        .next(opportunityPendingFor48hStep)
        .next(opportunityPendingFor96hStep)
        .next(opportunityPendingFor144hStep)
        .next(opportunityPendingFor192hStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // OPPORTUNITY PENDING FOR 24H STEP  =============================================================

  @Bean
  public Step opportunityPendingFor24hStep(
      ItemReader<Opportunity> opportunityPendingFor24hItemReader, MailFactory mailFactory,
      MailService mailService) {
    return stepBuilderFactory
        .get(PENDING_FOR_24H_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(opportunityPendingFor24hItemReader)
        .processor(new OpportunityPendingFor24hItemProcessor(mailFactory, mailService))
        .build();
  }

  @Bean
  public ItemReader<Opportunity> opportunityPendingFor24hItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunityItemReader(opportunityRepository,
        LocalDate.now().minusDays(2).atTime(12, 0), LocalDate.now().minusDays(1).atTime(12, 0));
  }

  // OPPORTUNITY PENDING FOR 48H STEP ==============================================================

  @Bean
  public Step opportunityPendingFor48hStep(
      ItemReader<Opportunity> opportunityPendingFor48hItemReader, MailFactory mailFactory,
      MailService mailService) {
    return stepBuilderFactory
        .get(PENDING_FOR_48H_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(opportunityPendingFor48hItemReader)
        .processor(new OpportunityPendingFor48hItemProcessor(mailFactory, mailService))
        .build();
  }

  @Bean
  public ItemReader<Opportunity> opportunityPendingFor48hItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunityItemReader(opportunityRepository,
        LocalDate.now().minusDays(3).atTime(12, 0), LocalDate.now().minusDays(2).atTime(12, 0));
  }

  // OPPORTUNITY PENDING FOR 96H STEP ==============================================================

  @Bean
  public Step opportunityPendingFor96hStep(
      ItemReader<Opportunity> opportunityPendingFor96hItemReader, MailFactory mailFactory,
      MailService mailService) {
    return stepBuilderFactory
        .get(PENDING_FOR_96H_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(opportunityPendingFor96hItemReader)
        .processor(new OpportunityPendingFor96hItemProcessor(mailFactory, mailService))
        .build();
  }

  @Bean
  public ItemReader<Opportunity> opportunityPendingFor96hItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunityItemReader(opportunityRepository,
        LocalDate.now().minusDays(5).atTime(12, 0), LocalDate.now().minusDays(4).atTime(12, 0));
  }

  // OPPORTUNITY PENDING FOR 144H STEP =============================================================

  @Bean
  public Step opportunityPendingFor144hStep(
      ItemReader<Opportunity> opportunityPendingFor144hItemReader, MailFactory mailFactory,
      MailService mailService) {
    return stepBuilderFactory
        .get(PENDING_FOR_144H_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(opportunityPendingFor144hItemReader)
        .processor(new OpportunityPendingFor144hItemProcessor(mailFactory, mailService))
        .build();
  }

  @Bean
  public ItemReader<Opportunity> opportunityPendingFor144hItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunityItemReader(opportunityRepository,
        LocalDate.now().minusDays(7).atTime(12, 0), LocalDate.now().minusDays(6).atTime(12, 0));
  }

  // OPPORTUNITY PENDING FOR 192H STEP =============================================================

  @Bean
  public Step opportunityPendingFor192hStep(
      ItemReader<Opportunity> opportunityPendingFor192hItemReader, MailFactory mailFactory,
      MailService mailService) {
    return stepBuilderFactory
        .get(PENDING_FOR_192H_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(opportunityPendingFor192hItemReader)
        .processor(new OpportunityPendingFor192hItemProcessor(mailFactory, mailService))
        .build();
  }

  @Bean
  public ItemReader<Opportunity> opportunityPendingFor192hItemReader(
      OpportunityRepository opportunityRepository) {
    return getPendingOpportunityItemReader(opportunityRepository,
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

  private ItemReader<Opportunity> getPendingOpportunityItemReader(
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
