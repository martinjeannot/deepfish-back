package com.deepfish.batch;

import com.deepfish.batch.steps.auth.AuthenticationTasklet;
import com.deepfish.batch.steps.auth.ClearAuthenticationTasklet;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  private final StepBuilderFactory stepBuilderFactory;

  public BatchConfiguration(StepBuilderFactory stepBuilderFactory) {
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
    jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
    return jobRegistryBeanPostProcessor;
  }

  @Bean
  public ItemReader<Talent> talentItemReader(TalentRepository talentRepository) {
    RepositoryItemReader<Talent> reader = new RepositoryItemReader<>();
    reader.setRepository(talentRepository);
    reader.setMethodName("findAll");
    reader.setPageSize(100);
    Map<String, Direction> sorts = new HashMap<>();
    sorts.put("createdAt", Direction.DESC);
    reader.setSort(sorts);
    return reader;
  }

  @Bean
  public Step authenticationStep(AuthenticationTasklet authenticationTasklet) {
    return stepBuilderFactory
        .get("authenticationStep")
        .tasklet(authenticationTasklet)
        .build();
  }

  @Bean
  public Step clearAuthenticationStep(ClearAuthenticationTasklet clearAuthenticationTasklet) {
    return stepBuilderFactory
        .get("clearAuthenticationStep")
        .tasklet(clearAuthenticationTasklet)
        .build();
  }

  // SCHEDULING ====================================================================================

  /**
   * Static factory for creating a new JobDetailFactoryBean for the given job
   *
   * @param jobName the job name
   * @return {@link JobDetailFactoryBean}
   */
  public static JobDetailFactoryBean getJobDetailFactoryBean(String jobName) {
    JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
    jobDetailFactoryBean.setJobClass(QuartzJobLauncher.class);
    // Jobs added with no trigger must be durable
    jobDetailFactoryBean.setDurability(true);
    Map<String, Object> jobData = new HashMap<>();
    jobData.put("jobName", jobName);
    jobDetailFactoryBean.setJobDataAsMap(jobData);
    return jobDetailFactoryBean;
  }

  /**
   * Static factory for creating a new CronTriggerFactoryBean
   *
   * @param jobDetailFactoryBean the {@link JobDetailFactoryBean} of the job
   * @param cronExpression the cron expression
   * @return {@link CronTriggerFactoryBean}
   */
  public static CronTriggerFactoryBean getCronTriggerFactoryBean(
      JobDetailFactoryBean jobDetailFactoryBean, String cronExpression) {
    CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
    triggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
    triggerFactoryBean.setCronExpression(cronExpression);
    return triggerFactoryBean;
  }
}
