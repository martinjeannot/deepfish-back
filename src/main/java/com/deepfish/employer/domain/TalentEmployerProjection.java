package com.deepfish.employer.domain;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "talent", types = {Employer.class})
public interface TalentEmployerProjection {

  String getFirstName();
}
