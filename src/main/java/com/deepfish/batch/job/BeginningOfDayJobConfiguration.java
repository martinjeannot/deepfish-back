package com.deepfish.batch.job;

import com.deepfish.batch.BatchConfiguration;
import com.deepfish.batch.item.processor.IncompleteProfileNotifier;
import com.deepfish.batch.item.processor.TalentReactivator;
import com.deepfish.batch.item.reader.TalentItemReader;
import com.deepfish.batch.item.writer.TalentItemWriter;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.time.Clock;
import java.time.LocalDate;
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
public class BeginningOfDayJobConfiguration {

  private static final String JOB_NAME = "beginningOfDayJob";

  public static final String SECOND_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME = "bod2ndIncompleteProfileNotificationStep";

  private static final int SECOND_INCOMPLETE_PROFILE_NOTIFICATION_DAY = 3;

  public static final String THIRD_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME = "bod3rdIncompleteProfileNotificationStep";

  private static final int THIRD_INCOMPLETE_PROFILE_NOTIFICATION_DAY = 14;

  public static final String FOURTH_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME = "bod4thIncompleteProfileNotificationStep";

  private static final int FOURTH_INCOMPLETE_PROFILE_NOTIFICATION_DAY = 17;

  private static final String TALENT_REACTIVATION_STEP_NAME = "bodTalentReactivationStep";

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  public BeginningOfDayJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory
  ) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // BEGINNING OF DAY JOB ==========================================================================

  @Bean
  public Job beginningOfDayJob(
      Step authenticationStep,
      Step bod2ndIncompleteProfileNotificationStep,
      Step bod3rdIncompleteProfileNotificationStep,
      Step bod4thIncompleteProfileNotificationStep,
      Step bodTalentReactivationStep,
      Step clearAuthenticationStep
  ) {
    return jobBuilderFactory
        .get(JOB_NAME)
        .start(authenticationStep)
        //.next(bod2ndIncompleteProfileNotificationStep)
        //.next(bod3rdIncompleteProfileNotificationStep)
        //.next(bod4thIncompleteProfileNotificationStep)
        .next(bodTalentReactivationStep)
        .next(clearAuthenticationStep)
        .build();
  }

  // 2ND INCOMPLETE PROFILE NOTIFICATION STEP ======================================================

  @JobScope
  @Bean
  public Step bod2ndIncompleteProfileNotificationStep(
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    return stepBuilderFactory
        .get(SECOND_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME)
        .<Talent, Talent>chunk(100)
        .reader(TalentItemReader
            .newInstance(
                talentRepository,
                LocalDate.now(Clock.systemUTC())
                    .minusDays(SECOND_INCOMPLETE_PROFILE_NOTIFICATION_DAY).atStartOfDay(),
                LocalDate.now(Clock.systemUTC())
                    .minusDays(SECOND_INCOMPLETE_PROFILE_NOTIFICATION_DAY - 1).atStartOfDay(),
                true))
        .processor(new IncompleteProfileNotifier(mailFactory, mailService))
        .writer(TalentItemWriter.newInstance(talentRepository))
        .build();
  }

  // 3RD INCOMPLETE PROFILE NOTIFICATION STEP ======================================================

  @JobScope
  @Bean
  public Step bod3rdIncompleteProfileNotificationStep(
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    return stepBuilderFactory
        .get(THIRD_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME)
        .<Talent, Talent>chunk(100)
        .reader(TalentItemReader
            .newInstance(
                talentRepository,
                LocalDate.now(Clock.systemUTC())
                    .minusDays(THIRD_INCOMPLETE_PROFILE_NOTIFICATION_DAY).atStartOfDay(),
                LocalDate.now(Clock.systemUTC())
                    .minusDays(THIRD_INCOMPLETE_PROFILE_NOTIFICATION_DAY - 1).atStartOfDay(),
                true))
        .processor(new IncompleteProfileNotifier(mailFactory, mailService))
        .writer(TalentItemWriter.newInstance(talentRepository))
        .build();
  }

  // 4TH INCOMPLETE PROFILE NOTIFICATION STEP ======================================================

  @JobScope
  @Bean
  public Step bod4thIncompleteProfileNotificationStep(
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    return stepBuilderFactory
        .get(FOURTH_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME)
        .<Talent, Talent>chunk(100)
        .reader(TalentItemReader
            .newInstance(
                talentRepository,
                LocalDate.now(Clock.systemUTC())
                    .minusDays(FOURTH_INCOMPLETE_PROFILE_NOTIFICATION_DAY).atStartOfDay(),
                LocalDate.now(Clock.systemUTC())
                    .minusDays(FOURTH_INCOMPLETE_PROFILE_NOTIFICATION_DAY - 1).atStartOfDay(),
                true))
        .processor(new IncompleteProfileNotifier(mailFactory, mailService))
        .writer(TalentItemWriter.newInstance(talentRepository))
        .build();
  }

  // TALENT REACTIVATION STEP ======================================================================

  @JobScope
  @Bean
  public Step bodTalentReactivationStep(
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    return stepBuilderFactory
        .get(TALENT_REACTIVATION_STEP_NAME)
        .<Talent, Talent>chunk(100)
        .reader(
            TalentItemReader
                .newInstance(
                    talentRepository,
                    LocalDate.now(Clock.systemUTC()),
                    false))
        .processor(new TalentReactivator(mailFactory, mailService))
        .writer(TalentItemWriter.newInstance(talentRepository))
        .build();
  }

  // SCHEDULING ====================================================================================

  @Bean
  public JobDetailFactoryBean beginningOfDayJobDetailFactoryBean() {
    return BatchConfiguration.getJobDetailFactoryBean(JOB_NAME);
  }

  @Bean
  public CronTriggerFactoryBean beginningOfDayJobTriggerFactoryBean() {
    return BatchConfiguration
        .getCronTriggerFactoryBean(
            beginningOfDayJobDetailFactoryBean(),
            //"0 0/1 * ? * * *" // every minute
            "0 17 8 ? * * *" // at 8:17AM every day
        );
  }
}
