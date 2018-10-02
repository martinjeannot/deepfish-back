package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.employer.repositories.RequirementRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * {@link RequirementService} default implementation
 */
@Service
public class DefaultRequirementService implements RequirementService {

  @Value("${deepfish.typeform.requirement.public.name}")
  private String requirementNameTypeformId;

  private final RequirementRepository requirementRepository;

  public DefaultRequirementService(
      RequirementRepository requirementRepository) {
    this.requirementRepository = requirementRepository;
  }

  @Override
  public Requirement saveNewRequirementFromTypeform(Map<String, Object> typeform,
      Employer employer) {
    Requirement requirement = new Requirement();
    requirement.setCreatedBy(employer.getId());
    requirement.setCompany(employer.getCompany());
    Optional<Map> requirementNameAnswer = ((List<Map>) ((Map) typeform.get("form_response"))
        .get("answers")).stream()
        .filter(answer -> ((Map) answer.get("field")).get("id").equals(requirementNameTypeformId))
        .findFirst();
    String requirementName =
        requirementNameAnswer.isPresent() ? requirementNameAnswer.get().get("text").toString()
            : "Mon nouveau besoin";
    requirement.setName(requirementName);
    requirement.setTypeform(typeform);
    return requirementRepository.save(requirement);
  }
}
