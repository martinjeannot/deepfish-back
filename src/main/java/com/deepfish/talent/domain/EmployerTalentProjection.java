package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.qualification.Qualification;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer", types = {Talent.class})
public interface EmployerTalentProjection {

  UUID getId();

  String getFirstName();

  String getLastName();

  Map<String, Object> getProfile();

  int getYearsOfExperience();

  Conditions getConditions();

  Qualification getQualification();
}
