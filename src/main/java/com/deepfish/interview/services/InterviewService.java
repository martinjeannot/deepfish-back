package com.deepfish.interview.services;

import com.deepfish.interview.domain.Interview;
import java.util.List;

/**
 * Service related to the {@link com.deepfish.interview.domain.Interview} domain object
 */
public interface InterviewService {

  Iterable<Interview> scheduleInterviews(Iterable<Interview> interviews);

  Iterable<Interview> scheduleInterviewsAsAdmin(Iterable<Interview> interviews);

  void cancelInterview(
      Interview interview,
      String cancelledBy,
      boolean cancelLinkedInterviews
  );

  Interview updateInterviewStatus(Interview interview);

  /**
   * Handle employer declination of the given interviews
   *
   * @param declinedInterviews the declined interviews
   */
  void handleEmployerDeclination(List<Interview> declinedInterviews);
}
