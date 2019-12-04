package com.deepfish.talent.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {EmployerQueryableTalent.class})
public interface DefaultEmployerQueryableTalentProjection {

  UUID getId();

  String getFirstName();

  String getProfilePictureUrl();

  Map<String, Object> getFullProfile();

  Integer getYearsOfExperience();

  @Value("#{target.getConditions().getFixedSalary()}")
  BigDecimal getBaseSalary();
}
