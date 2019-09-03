package com.deepfish.interview.services;

import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.domain.InterviewStatus;
import com.deepfish.interview.repositories.InterviewRepository;
import com.google.common.collect.Lists;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultAvailabilityService implements AvailabilityService {

  private static final long INTERVIEW_SCHEDULING_SLOT_INTERVAL = 30; // in minutes

  private static final List<InterviewStatus> UNAVAILABLE_SLOT_STATUSES = Lists.newArrayList(
      InterviewStatus.TENTATIVE,
      InterviewStatus.CONFIRMED
  );

  private final InterviewRepository interviewRepository;

  public DefaultAvailabilityService(
      InterviewRepository interviewRepository
  ) {
    this.interviewRepository = interviewRepository;
  }

  @Override
  public Map<ZonedDateTime, Map> getUnavailableSlots(
      UUID employerId,
      UUID talentId
  ) {
    ZonedDateTime now = ZonedDateTime.now(Clock.systemUTC());
    List<Interview> unavailableEmployerSlots = interviewRepository
        .findByEmployerIdAndStartDateTimeAfterAndStatusIn(
            employerId,
            now,
            UNAVAILABLE_SLOT_STATUSES
        );
    List<Interview> unavailableTalentSlots = interviewRepository
        .findByTalentIdAndStartDateTimeAfterAndStatusIn(
            talentId,
            now,
            UNAVAILABLE_SLOT_STATUSES
        );
    // employer unavailability reasons have priority over talent's one
    // hence this merge order
    unavailableTalentSlots.addAll(unavailableEmployerSlots);
    Map<ZonedDateTime, Map> unavailableSlots = new HashMap<>();
    for (Interview interview : unavailableTalentSlots) {
      ZonedDateTime currentSlot = interview.getStartAt();
      // mark slots as unavailable for the whole duration of the interview
      while (!interview.getEndAt().equals(currentSlot)) {
        // add new unavailability reason
        Map<String, Object> unavailabilityReason = new HashMap<>();
        // TODO improve for multi-account company
        unavailabilityReason.put("employerId", interview.getEmployer().getId());
        unavailabilityReason.put("talent",
            interview.getTalent().getFirstName() + " "
                + interview.getTalent().getLastName().substring(0, 1) + ".");
        unavailabilityReason.put("status", interview.getStatus().toString());
        unavailableSlots.put(currentSlot, unavailabilityReason);
        // compute next slot
        currentSlot = currentSlot.plusMinutes(INTERVIEW_SCHEDULING_SLOT_INTERVAL);
      }
    }
    return unavailableSlots;
  }
}
