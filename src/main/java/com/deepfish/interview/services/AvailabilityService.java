package com.deepfish.interview.services;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Service related to the (un)availability of interview scheduling slots
 */
public interface AvailabilityService {

  Map<ZonedDateTime, Map> getUnavailableSlots(
      UUID employerId,
      UUID talentId
  );
}
