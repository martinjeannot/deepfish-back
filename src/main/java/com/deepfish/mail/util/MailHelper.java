package com.deepfish.mail.util;

import com.deepfish.interview.domain.InterviewFormat;

public class MailHelper {

  public static String getLabelForInterviewFormat(InterviewFormat interviewFormat) {
    switch (interviewFormat) {
      case PHONE:
        return "téléphonique";
      case VIDEO:
        return "vidéo";
      case IN_PERSON:
        return "physique";
      default:
        return interviewFormat.toString();
    }
  }
}
