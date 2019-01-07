package com.deepfish.batch.job;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.item.processor.OpportunityExpirator;
import com.deepfish.batch.item.reader.OpportunityItemReader;
import com.deepfish.batch.item.writer.OpportunityItemWriter;
import com.deepfish.batch.tasklet.OpportunityDatumSamplingTasklet;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.repositories.OpportunityRepository;
import java.time.Clock;
import java.time.LocalDateTime;
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

  private static final String PENDING_OPPORTUNITY_EXPIRATION_STEP_NAME = "eodPendingOpportunityExpirationStep";

  private static final String OPPORTUNITY_DATUM_SAMPLING_STEP_NAME = "eodOpportunityDatumSamplingStep";

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
      Step eodPendingOpportunityExpirationStep,
      Step eodOpportunityDatumSamplingStep,
      Step clearAuthenticationStep
  ) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(eodPendingOpportunityExpirationStep)
        .next(eodOpportunityDatumSamplingStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // PENDING OPPORTUNITY EXPIRATION STEP ===========================================================

  @JobScope
  @Bean
  public Step eodPendingOpportunityExpirationStep(
      OpportunityRepository opportunityRepository
  ) {
    return stepBuilderFactory
        .get(PENDING_OPPORTUNITY_EXPIRATION_STEP_NAME)
        .<Opportunity, Opportunity>chunk(100)
        .reader(OpportunityItemReader
            .newInstance(
                opportunityRepository,
                LocalDateTime.now(Clock.systemUTC()).minusDays(Opportunity.LIFESPAN_DAYS),
                OpportunityStatus.PENDING))
        .processor(new OpportunityExpirator())
        .writer(OpportunityItemWriter.newInstance(opportunityRepository))
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
            "0 0 23 ? * * *" // at 23:00PM every day
        );
  }
}
