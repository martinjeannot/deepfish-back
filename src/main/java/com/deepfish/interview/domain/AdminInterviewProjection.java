package com.deepfish.interview.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.talent.domain.Talent;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "admin", types = {Interview.class})
public interface AdminInterviewProjection {

  UUID getId();

  InterviewStatus getStatus();

  LocalDateTime getCreatedAt();

  String getLocation();

  ZonedDateTime getStartAt();

  ZonedDateTime getEndAt();

  InterviewFormat getFormat();

  Talent getTalent();

  ParticipationStatus getTalentResponseStatus();

  @Value("#{target.employer.company}")
  Company getCompany();

  ParticipationStatus getEmployerResponseStatus();
}
