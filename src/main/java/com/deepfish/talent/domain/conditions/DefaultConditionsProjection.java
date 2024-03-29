package com.deepfish.talent.domain.conditions;

import com.deepfish.company.domain.Company;
import com.deepfish.company.domain.CompanyMaturityLevel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Conditions.class})
public interface DefaultConditionsProjection {

  BigDecimal getFixedSalary();

  LocalDate getCanStartOn();

  String getAvailableFrom();

  boolean isInternship();

  Set<Company> getCompanyBlacklist();

  Set<CompanyMaturityLevel> getCompanyMaturityLevels();

  Set<JobType> getJobTypes();

  Set<CommodityType> getCommodityTypes();

  Set<TaskType> getTaskTypes();

  Set<IndustryType> getIndustryTypes();

  Set<ClientIndustryType> getClientIndustryTypes();

  Set<DefaultFixedLocationsProjection> getFixedLocations();
}
