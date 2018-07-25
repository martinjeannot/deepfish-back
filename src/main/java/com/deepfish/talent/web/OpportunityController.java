package com.deepfish.talent.web;

import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.repositories.OpportunityRepository;
import com.deepfish.talent.services.OpportunityService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class OpportunityController {

  private final OpportunityService opportunityService;

  private final OpportunityRepository opportunityRepository;

  public OpportunityController(OpportunityService opportunityService,
      OpportunityRepository opportunityRepository) {
    this.opportunityService = opportunityService;
    this.opportunityRepository = opportunityRepository;
  }

  @PostMapping("/talents/{talentId}/opportunities/bulk-declination")
  @ResponseBody
  public ResponseEntity declineInBulk(@PathVariable("talentId") UUID talentId) {
    opportunityService.declineInBulk(talentId);
    return ResponseEntity.ok(null);
  }

  @GetMapping("opportunities/counts")
  @ResponseBody
  public ResponseEntity getCountsByRequirementId(
      @RequestParam("requirementId") UUID requirementId) {
    Map<String, Long> results = new HashMap<>();
    long acceptedCount = opportunityRepository
        .countByRequirementIdAndTalentStatus(requirementId, OpportunityStatus.ACCEPTED);
    results.put("acceptedCount", acceptedCount);
    long pendingCount = opportunityRepository
        .countByRequirementIdAndTalentStatus(requirementId, OpportunityStatus.PENDING);
    results.put("pendingCount", pendingCount);
    long declinedCount = opportunityRepository
        .countByRequirementIdAndTalentStatus(requirementId, OpportunityStatus.DECLINED);
    results.put("declinedCount", declinedCount);
    results.put("total", acceptedCount + pendingCount + declinedCount);
    return ResponseEntity.ok(results);
  }
}
