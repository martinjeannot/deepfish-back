package com.deepfish.batch.item.processor;

import com.deepfish.batch.job.BeginningOfDayJobConfiguration;
import com.deepfish.batch.job.IntradayJobConfiguration;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import org.simplejavamail.email.Email;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

public class IncompleteProfileNotifier implements ItemProcessor<Talent, Talent> {

  private final MailFactory mailFactory;

  private final MailService mailService;

  private StepExecution stepExecution;

  public IncompleteProfileNotifier(
      MailFactory mailFactory,
      MailService mailService
  ) {
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @Override
  public Talent process(Talent talent) throws Exception {
    talent.calculateProfileCompleteness();
    if (talent.getProfileCompleteness() <= 60) { // incomplete profile
      Email incompleteProfileMail;
      switch (stepExecution.getStepName()) {
        case IntradayJobConfiguration.FIRST_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME:
          incompleteProfileMail = mailFactory.getTalent1stIncompleteProfileMail(talent);
          break;
        case BeginningOfDayJobConfiguration.SECOND_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME:
          incompleteProfileMail = mailFactory.getTalent2ndIncompleteProfileMail(talent);
          break;
        case BeginningOfDayJobConfiguration.THIRD_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME:
          incompleteProfileMail = mailFactory.getTalent3rdIncompleteProfileMail(talent);
          break;
        case BeginningOfDayJobConfiguration.FOURTH_INCOMPLETE_PROFILE_NOTIFICATION_STEP_NAME:
          talent.deactivate();
          incompleteProfileMail = mailFactory.getTalent4thIncompleteProfileMail(talent);
          break;
        default:
          throw new IllegalStateException("Unknown step: " + stepExecution.getStepName());
      }
      mailService.send(incompleteProfileMail);
    }
    return talent;
  }

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }
}
