package com.deepfish.talent.domain.qualification;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer", types = {Qualification.class})
public interface EmployerQualificationProjection {

  String getRecommendation();
}
