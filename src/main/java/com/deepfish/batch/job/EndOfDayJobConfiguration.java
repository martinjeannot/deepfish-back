package com.deepfish.batch.job;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.tasklet.LinkedinProfileScrapingTasklet;
import com.deepfish.batch.tasklet.OpportunityDatumSamplingTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class EndOfDayJobConfiguration {

  private static final String JOB_NAME = "endOfDayJob";

  private static final String OPPORTUNITY_DATUM_SAMPLING_STEP_NAME = "eodOpportunityDatumSamplingStep";

  private static final String LINKEDIN_PROFILE_SCRAPING_STEP_NAME = "eodLinkedinProfileScrapingStep";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public EndOfDayJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory
  ) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // END OF DAY JOB ================================================================================

  @Bean
  public Job endOfDayJob(
      Step authenticationStep,
      Step eodOpportunityDatumSamplingStep,
      Step eodLinkedinProfileScrapingStep,
      Step clearAuthenticationStep
  ) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(eodOpportunityDatumSamplingStep)
        .next(eodLinkedinProfileScrapingStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // OPPORTUNITY DATUM SAMPLING STEP ===============================================================

  @JobScope
  @Bean
  public Step eodOpportunityDatumSamplingStep(
      OpportunityDatumSamplingTasklet opportunityDatumSamplingTasklet
  ) {
    return stepBuilderFactory
        .get(OPPORTUNITY_DATUM_SAMPLING_STEP_NAME)
        .tasklet(opportunityDatumSamplingTasklet)
        .build();
  }

  // LINKEDIN PROFILE SCRAPING STEP ================================================================

  @JobScope
  @Bean
  public Step eodLinkedinProfileScrapingStep(
      LinkedinProfileScrapingTasklet linkedinProfileScrapingTasklet
  ) {
    return stepBuilderFactory
        .get(LINKEDIN_PROFILE_SCRAPING_STEP_NAME)
        .tasklet(linkedinProfileScrapingTasklet)
        .build();
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean endOfDayJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean endOfDayJobTriggerFactoryBean() {
    return BatchConfiguration
        .getCronTriggerFactoryBean(
            endOfDayJobDetailFactoryBean(),
            //"0 0/1 * ? * * *" // every minute
            "0 17 23 ? * * *" // at 23:17PM every day
        );
  }
}
