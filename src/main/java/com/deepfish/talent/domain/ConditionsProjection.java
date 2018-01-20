package com.deepfish.talent.domain;

import com.deepfish.company.domain.CompanyMaturityLevel;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Conditions.class})
public interface ConditionsProjection {

  Set<CompanyMaturityLevel> getCompanyMaturityLevels();

  Set<Job> getJobs();

  Set<CommodityType> getCommodityTypes();

  Set<TaskType> getTaskTypes();
}
