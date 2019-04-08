package com.deepfish.batch.job;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.item.processor.TalentQualificationInterviewSchedulingNotifier;
import com.deepfish.batch.item.reader.TalentItemReader;
import com.deepfish.batch.item.writer.NoOpItemWriter;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.sql.DataSource;
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
public class HourlyJobConfiguration {

  private static final String JOB_NAME = "hourlyJob";

  private static final String TALENT_QUALIFICATION_INTERVIEW_SCHEDULING_NOTIFICATION_STEP_NAME = "hourlyTalentQualificationInterviewSchedulingNotificationStep";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public HourlyJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory
  ) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // HOURLY JOB ====================================================================================

  @Bean
  public Job hourlyJob(
      Step authenticationStep,
      Step hourlyTalentQualificationInterviewSchedulingNotificationStep,
      Step clearAuthenticationStep
  ) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        .next(hourlyTalentQualificationInterviewSchedulingNotificationStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // TALENT QUALIFICATION INTERVIEW SCHEDULING NOTIFICATION STEP ===================================

  @JobScope
  @Bean
  public Step hourlyTalentQualificationInterviewSchedulingNotificationStep(
      DataSource dataSource,
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    return stepBuilderFactory
        .get(TALENT_QUALIFICATION_INTERVIEW_SCHEDULING_NOTIFICATION_STEP_NAME)
        .<UUID, Talent>chunk(100)
        .reader(TalentItemReader
            .newInstance(
                dataSource,
                LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.HOURS).minusHours(2),
                LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.HOURS).minusHours(1)
            ))
        .processor(new TalentQualificationInterviewSchedulingNotifier(
            talentRepository, mailFactory, mailService))
        .writer(new NoOpItemWriter<>())
        .build();
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean hourlyJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean hourlyJobTriggerFactoryBean() {
    return BatchConfiguration
        .getCronTriggerFactoryBean(
            hourlyJobDetailFactoryBean(),
            //"0 0/1 * ? * * *" // every minute
            "0 0 * ? * * *" // every hour
        );
  }
}
