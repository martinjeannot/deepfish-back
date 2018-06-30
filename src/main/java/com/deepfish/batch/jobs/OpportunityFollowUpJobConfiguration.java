package com.deepfish.batch.jobs;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.TalentItemProcessor;
import com.deepfish.talent.domain.Talent;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class OpportunityFollowUpJobConfiguration {

  private static final String JOB_NAME = "OpportunityFollowUpJob";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public OpportunityFollowUpJobConfiguration(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  public Job profileCompletenessComputationJob(Step authenticationStep,
      Step profileCompletenessComputationStep, Step clearAuthenticationStep) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(profileCompletenessComputationStep)
        .next(clearAuthenticationStep)
        .build();
  }

  @Bean
  public Step profileCompletenessComputationStep(ItemReader<Talent> talentItemReader) {
    return stepBuilderFactory
        .get("profileCompletenessComputationStep")
        .<Talent, Talent>chunk(100)
        .reader(talentItemReader)
        .processor(new TalentItemProcessor())
        .build();
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean opportunityFollowUpJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean opportunityFollowUpJobTriggerFactoryBean() {
    // job is scheduled after every 1 minute
    return BatchConfiguration
        .getCronTriggerFactoryBean(opportunityFollowUpJobDetailFactoryBean(), "0 0/1 * 1/1 * ? *");
  }
}
