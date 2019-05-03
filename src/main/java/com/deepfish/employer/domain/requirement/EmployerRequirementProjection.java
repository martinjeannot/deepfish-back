package com.deepfish.employer.domain.requirement;

import com.deepfish.talent.domain.conditions.JobType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer", types = {Requirement.class})
public interface EmployerRequirementProjection {

  LocalDateTime getCreatedAt();

  String getName();

  RequirementStatus getStatus();

  Map<String, Object> getTypeform();

  JobType getJobType();

  Seniority getSeniority();

  String getLocation();

  BigDecimal getFixedSalary();
}
