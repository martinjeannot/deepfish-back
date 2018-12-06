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

  public static final String FIRST_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME = "intraday1stIncompleteProfileNotificationStep";

  private static final int FIRST_INCOMPLETE_PROFILE_NOTIFICATION_HOUR = 3;

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
      Step intraday1stIncompleteProfileNotificationStep,
      Step clearAuthenticationStep
  ) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(intraday1stIncompleteProfileNotificationStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // 1ST INCOMPLETE PROFILE NOTIFICATION STEP ======================================================

  @JobScope
  @Bean
  public Step intraday1stIncompleteProfileNotificationStep(
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    return stepBuilderFactory
        .get(FIRST_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME)
        .<Talent, Talent>chunk(100)
        .reader(TalentItemReader
            .newInstance(
                talentRepository,
                LocalDateTime.now(Clock.systemUTC())
                    .minusHours(FIRST_INCOMPLETE_PROFILE_NOTIFICATION_HOUR)
                    .truncatedTo(ChronoUnit.HOURS),
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
