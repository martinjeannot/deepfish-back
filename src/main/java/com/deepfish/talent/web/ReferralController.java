package com.deepfish.talent.web;

import com.deepfish.talent.services.ReferralService;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController
public class ReferralController {

  private final ReferralService referralService;

  public ReferralController(
      ReferralService referralService
  ) {
    this.referralService = referralService;
  }

  @PostMapping("/talents/{talentId}/send-referral-invitations")
  public ResponseEntity sendInvitations(
      @PathVariable("talentId") UUID talentId,
      @RequestBody Map<String, Object> body
  ) {
    referralService.sendInvitations(
        talentId,
        body.get("emails").toString(),
        body.get("subject").toString(),
        body.get("message").toString());
    return ResponseEntity.ok().build();
  }
}
