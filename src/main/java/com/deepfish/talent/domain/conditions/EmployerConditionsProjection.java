package com.deepfish.talent.domain.conditions;

import java.math.BigDecimal;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer", types = {Conditions.class})
public interface EmployerConditionsProjection {

  BigDecimal getFixedSalary();

  Set<TaskType> getTaskTypes();
}
