package com.deepfish.employer.web;

import java.util.Map;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@Validated
public class RequirementController {

  public RequirementController() {
  }

  @PostMapping("requirements/typeform")
  @ResponseBody
  public ResponseEntity handleNewRequirementTypeformSubmission(
      @RequestBody Map<String, Object> typeform) {
    return ResponseEntity.ok().build();
  }
}
