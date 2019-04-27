package com.deepfish.talent.web;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.repositories.OpportunityRepository;
import com.deepfish.talent.services.TalentService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class OpportunityController {

  private final MailService mailService;

  private final MailFactory mailFactory;

  private final TalentService talentService;

  private final OpportunityRepository opportunityRepository;

  public OpportunityController(
      MailService mailService,
      MailFactory mailFactory,
      TalentService talentService,
      OpportunityRepository opportunityRepository) {
    this.mailService = mailService;
    this.mailFactory = mailFactory;
    this.talentService = talentService;
    this.opportunityRepository = opportunityRepository;
  }

  @PostMapping("/opportunities/{opportunityId}/ask-question")
  public ResponseEntity askQuestionToAdmins(
      @PathVariable("opportunityId") UUID opportunityId,
      @RequestBody Map<String, Object> body
  ) {
    Opportunity opportunity = opportunityRepository.findOne(opportunityId);
    mailService.send(
        mailFactory.getAdminTalentQuestionMail(
            opportunity,
            body.get("question").toString()));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/talents/{talentId}/opportunities/bulk-declination")
  @ResponseBody
  public ResponseEntity declineInBulk(@PathVariable("talentId") UUID talentId,
      @RequestBody Map<String, Object> body) {
    if (!body.containsKey("bulkDeclinationReason")) {
      throw new IllegalArgumentException("No bulk declination reason found");
    }
    talentService.deactivate(talentId, (String) body.get("bulkDeclinationReason"));
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
