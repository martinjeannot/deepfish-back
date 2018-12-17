package com.deepfish.interview.domain;

import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * See https://tools.ietf.org/html/rfc5545
 */
public class Interview {

  private UUID id;

  private InterviewStatus status = InterviewStatus.TENTATIVE;

  private LocalDateTime createdAt = LocalDateTime.now(Clock.systemUTC());

  private LocalDateTime updatedAt = LocalDateTime.now(Clock.systemUTC());

  private String summary;

  private String description;

  private String location;

  private ZonedDateTime startAt;

  private ZonedDateTime endAt;

  private InterviewType interviewType;

  // Attendees

  private Talent talent;

  private AttendeeResponseStatus talentReponseStatus = AttendeeResponseStatus.NEEDS_ACTION;

  private Employer employer;

  private AttendeeResponseStatus employerResponseStatus = AttendeeResponseStatus.NEEDS_ACTION;
}
