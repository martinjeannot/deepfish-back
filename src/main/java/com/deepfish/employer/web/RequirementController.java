package com.deepfish.employer.web;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.employer.services.RequirementService;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@Validated
public class RequirementController {

  private final RequirementService requirementService;

  private final EmployerRepository employerRepository;

  public RequirementController(
      RequirementService requirementService,
      EmployerRepository employerRepository) {
    this.requirementService = requirementService;
    this.employerRepository = employerRepository;
  }

  @PostMapping("requirements/typeform")
  @ResponseBody
  public ResponseEntity createNewRequirementFromTypeform(
      @RequestBody Map<String, Object> typeform) {
    // retrieve current employer
    Object employerHiddenField = ((Map) ((Map) typeform.get("form_response")).get("hidden"))
        .get("employer");
    if (!Objects.nonNull(employerHiddenField)) {
      throw new IllegalArgumentException("Missing employer id");
    }
    UUID employerId = UUID.fromString(employerHiddenField.toString());
    Employer employer = employerRepository.findOne(employerId);

    requirementService.saveNewRequirementFromTypeform(typeform, employer);

    return ResponseEntity.ok().build();
  }
}
