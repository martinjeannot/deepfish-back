package com.deepfish.employer.domain;

import com.deepfish.talent.domain.conditions.Job;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Requirement.class})
public interface DefaultRequirementProjection {

  LocalDateTime getCreatedAt();

  String getName();

  Job getJob();

  Seniority getSeniority();

  String getLocation();

  BigDecimal getFixedSalary();
}
