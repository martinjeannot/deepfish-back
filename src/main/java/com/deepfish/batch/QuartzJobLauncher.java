package com.deepfish.batch;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobLauncher extends QuartzJobBean {

  private JobLocator jobLocator;

  private JobLauncher jobLauncher;

  @Autowired
  public void setJobLocator(JobLocator jobLocator) {
    this.jobLocator = jobLocator;
  }

  @Autowired
  public void setJobLauncher(JobLauncher jobLauncher) {
    this.jobLauncher = jobLauncher;
  }

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    String jobName = context.getMergedJobDataMap().getString("jobName");
    try {
      jobLauncher.run(
          jobLocator.getJob(jobName),
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters());
    } catch (org.springframework.batch.core.JobExecutionException e) {
      throw new JobExecutionException(e);
    }
  }
}
