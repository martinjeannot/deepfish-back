package com.deepfish.batch.job;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.item.processor.IncompleteProfileNotifier;
import com.deepfish.batch.item.reader.TalentItemReader;
import com.deepfish.batch.item.writer.TalentItemWriter;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
public class IntradayJobConfiguration {

  private static final String JOB_NAME = "intradayJob";

  private static final String INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME = "intradayIncompleteProfileNotificationStep";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public IntradayJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory
  ) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // INTRADAY JOB ==================================================================================

  @Bean
  public Job intradayJob(
      Step authenticationStep,
      Step intradayIncompleteProfileNotificationStep,
      Step clearAuthenticationStep
  ) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(intradayIncompleteProfileNotificationStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // PROFILE COMPLETENESS CALCULATION STEP =========================================================

  @JobScope
  @Bean
  public Step intradayIncompleteProfileNotificationStep(
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    return stepBuilderFactory
        .get(INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME)
        .<Talent, Talent>chunk(100)
        .reader(TalentItemReader
            .newInstance(
                talentRepository,
                LocalDateTime.now(Clock.systemUTC()).minusHours(4).truncatedTo(ChronoUnit.HOURS),
                LocalDateTime.now(Clock.systemUTC()).minusHours(1).truncatedTo(ChronoUnit.HOURS)))
        .processor(new IncompleteProfileNotifier(mailFactory, mailService))
        .writer(TalentItemWriter.newInstance(talentRepository))
        .build();
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean intradayJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean intradayJobTriggerFactoryBean() {
    return BatchConfiguration
        .getCronTriggerFactoryBean(
            intradayJobDetailFactoryBean(),
            //"0 0/1 * 1/1 * ? *"
            "0 0 7 1/1 * ? *"
        );
  }
}
