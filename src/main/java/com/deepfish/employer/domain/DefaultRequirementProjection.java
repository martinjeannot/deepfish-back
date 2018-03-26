package com.deepfish.employer.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.talent.domain.conditions.Job;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Requirement.class})
public interface DefaultRequirementProjection {

  UUID getId();

  LocalDateTime getCreatedAt();

  Company getCompany();

  String getName();

  Job getJob();

  Seniority getSeniority();

  String getLocation();

  BigDecimal getFixedSalary();
}
