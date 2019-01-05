package com.deepfish.interview.services;

import com.deepfish.interview.domain.Interview;

/**
 * Service related to the {@link com.deepfish.interview.domain.Interview} domain object
 */
public interface InterviewService {

  Iterable<Interview> scheduleInterviews(Iterable<Interview> interviews);
}
