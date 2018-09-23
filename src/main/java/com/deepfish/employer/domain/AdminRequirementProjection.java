package com.deepfish.employer.domain;

import java.util.Map;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "admin", types = {Requirement.class})
public interface AdminRequirementProjection extends DefaultRequirementProjection {

  Map<String, Object> getTypeform();

  String getNotes();
}
