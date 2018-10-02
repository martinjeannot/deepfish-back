package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import java.util.Map;

/**
 * Service related to {@link Requirement} entity
 */
public interface RequirementService {

  Requirement saveNewRequirementFromTypeform(Map<String, Object> typeform, Employer employer);
}
