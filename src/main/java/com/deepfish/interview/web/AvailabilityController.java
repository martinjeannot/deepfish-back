package com.deepfish.interview.web;

import com.deepfish.interview.services.AvailabilityService;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestController
@RequestMapping("/itw/availability")
public class AvailabilityController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityController.class);

  private final AvailabilityService availabilityService;

  public AvailabilityController(
      AvailabilityService availabilityService
  ) {
    this.availabilityService = availabilityService;
  }

  @GetMapping("/unavailable-slots")
  public ResponseEntity getUnavailableSlots(
      @RequestParam("employer-id") UUID employerId,
      @RequestParam("talent-id") UUID talentId
  ) {
    Map<ZonedDateTime, Map> unavailableSlots = availabilityService
        .getUnavailableSlots(employerId, talentId);
    return ResponseEntity.ok(unavailableSlots);
  }
}
