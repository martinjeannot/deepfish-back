package com.deepfish.talent.domain.conditions;

import com.deepfish.company.domain.CompanyMaturityLevel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Conditions.class})
public interface DefaultConditionsProjection {

  BigDecimal getFixedSalary();

  LocalDate getCanStartOn();

  Set<CompanyMaturityLevel> getCompanyMaturityLevels();

  Set<Job> getJobs();

  Set<CommodityType> getCommodityTypes();

  Set<TaskType> getTaskTypes();

  Set<DefaultFixedLocationsProjection> getFixedLocations();
}
