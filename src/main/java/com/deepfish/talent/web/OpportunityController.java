package com.deepfish.talent.web;

import com.deepfish.talent.services.OpportunityService;
import java.util.UUID;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class OpportunityController {

  private final OpportunityService opportunityService;

  public OpportunityController(
      OpportunityService opportunityService) {
    this.opportunityService = opportunityService;
  }

  @PostMapping("/talents/{talentId}/opportunities/bulk-declination")
  @ResponseBody
  public ResponseEntity declineInBulk(@PathVariable("talentId") UUID talentId) {
    opportunityService.declineInBulk(talentId);
    return ResponseEntity.ok(null);
  }
}
