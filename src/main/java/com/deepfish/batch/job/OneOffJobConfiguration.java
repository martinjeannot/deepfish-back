package com.deepfish.batch.job;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.item.processor.TalentMigrator;
import com.deepfish.batch.item.reader.TalentItemReader;
import com.deepfish.batch.item.writer.TalentItemWriter;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
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
public class OneOffJobConfiguration {

  private static final String JOB_NAME = "oneOffJob";

  private static final String ONE_OFF_STEP_NAME = "oneOffStep";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public OneOffJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory
  ) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // ONE-OFF JOB ===================================================================================

  @Bean
  public Job oneOffJob(
      Step authenticationStep,
      Step oneOffStep,
      Step clearAuthenticationStep
  ) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(oneOffStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // ONE-OFF STEP ==================================================================================

  @JobScope
  @Bean
  public Step oneOffStep(
      TalentRepository talentRepository
  ) {
    return stepBuilderFactory
        .get(ONE_OFF_STEP_NAME)
        .<Talent, Talent>chunk(100)
        .reader(TalentItemReader.newInstance(talentRepository))
        .processor(new TalentMigrator())
        .writer(TalentItemWriter.newInstance(talentRepository))
        .build();
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean oneOffJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean oneOffJobTriggerFactoryBean() {
    return BatchConfiguration
        .getCronTriggerFactoryBean(
            oneOffJobDetailFactoryBean(),
            //"0 0/1 * ? * * *" // every minute
            "0 30 23 28 FEB ? 2019" // on 28/02/2019 at 23:30PM
        );
  }
}
