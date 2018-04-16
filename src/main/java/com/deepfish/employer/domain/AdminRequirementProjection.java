package com.deepfish.employer.domain;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "admin", types = {Requirement.class})
public interface AdminRequirementProjection extends DefaultRequirementProjection {

  String getNotes();
}
