package com.deepfish.calendly.web;

import com.deepfish.security.SystemAuthentication;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendly/webhooks")
public class WebhookController {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class);

  private final String talentQualificationSlug;

  private final TalentRepository talentRepository;

  public WebhookController(
      @Value("${calendly.slugs.talent-qualification:#{null}}") String talentQualificationSlug,
      TalentRepository talentRepository
  ) {
    this.talentQualificationSlug = talentQualificationSlug;
    this.talentRepository = talentRepository;
  }

  @PostMapping("/invitee-created")
  public ResponseEntity inviteeCreated(
      @RequestBody Map<String, Object> payload
  ) {
    payload = (Map<String, Object>) payload.get("payload");
    String eventSlug = ((Map) payload.get("event_type")).get("slug").toString();

    SecurityContextHolder.getContext().setAuthentication(SystemAuthentication.getAuthentication());

    if (talentQualificationSlug.equals(eventSlug)) {
      Talent talent = findTalent((Map<String, Object>) payload.get("invitee"));
      if (Objects.nonNull(talent)) {
        talent.getQualification().setInterviewScheduled(true);
        talentRepository.save(talent);
      }
    }

    SecurityContextHolder.clearContext();

    return ResponseEntity.ok().build();
  }

  @PostMapping("/invitee-canceled")
  public ResponseEntity inviteeCanceled(
      @RequestBody Map<String, Object> payload
  ) {
    payload = (Map<String, Object>) payload.get("payload");
    String eventSlug = ((Map) payload.get("event_type")).get("slug").toString();

    SecurityContextHolder.getContext().setAuthentication(SystemAuthentication.getAuthentication());

    if (talentQualificationSlug.equals(eventSlug)) {
      Talent talent = findTalent((Map<String, Object>) payload.get("invitee"));
      if (Objects.nonNull(talent)) {
        talent.getQualification().setInterviewScheduled(false);
        talentRepository.save(talent);
      }
    }

    SecurityContextHolder.clearContext();

    return ResponseEntity.ok().build();
  }

  // TODO replace talent search by email/firstname/lastname with UUID when calendly implements hidden fields
  private Talent findTalent(Map<String, Object> invitee) {
    String email = invitee.get("email").toString();
    Talent talent = talentRepository.findByEmail(email);
    if (Objects.isNull(talent)) {
      talent = talentRepository.findByFirstNameAndLastName(
          invitee.get("first_name").toString(),
          invitee.get("last_name").toString()
      );
    }
    return talent;
  }
}
