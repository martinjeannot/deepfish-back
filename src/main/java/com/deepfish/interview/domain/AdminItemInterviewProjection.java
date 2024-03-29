package com.deepfish.interview.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.talent.domain.Talent;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "admin-item", types = {Interview.class})
public interface AdminItemInterviewProjection {

  UUID getId();

  InterviewStatus getStatus();

  LocalDateTime getCreatedAt();

  ZonedDateTime getStartAt();

  Talent getTalent();

  @Value("#{target.employer.company}")
  Company getCompany();
}
