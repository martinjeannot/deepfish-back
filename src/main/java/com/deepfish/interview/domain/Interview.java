package com.deepfish.interview.domain;

import com.deepfish.core.domain.StateRetaining;
import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.querydsl.core.annotations.QueryEntity;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.Identifiable;

/**
 * See https://tools.ietf.org/html/rfc5545
 */
@Entity
@QueryEntity
@Data
@ToString(exclude = {"opportunity", "talent", "employer"})
@EqualsAndHashCode(exclude = {"opportunity", "talent", "employer"})
public class Interview implements Identifiable<UUID>, StateRetaining {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  /**
   * Id shared between all slots of the same interview scheduling attempt
   */
  @NotNull
  private UUID sharedId;

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  private Map<String, Object> previousState;

  @NotNull
  @Enumerated(EnumType.STRING)
  private InterviewStatus status = InterviewStatus.TENTATIVE;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now(Clock.systemUTC());

  @NotNull
  private UUID creatorId;

  @NotNull
  private LocalDateTime updatedAt = LocalDateTime.now(Clock.systemUTC());

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_interview__opportunity__opportunity_id"))
  private Opportunity opportunity;

  @NotNull
  private String summary = "";

  @NotNull
  @Column(columnDefinition = "text")
  private String description = "";

  @NotNull
  @Column(columnDefinition = "text")
  private String location = "";

  @Transient
  private ZonedDateTime startAt;

  @NotNull
  private ZonedDateTime startDateTime;

  @NotNull
  private String startTimeZone;

  @Transient
  private ZonedDateTime endAt;

  @NotNull
  private ZonedDateTime endDateTime;

  @NotNull
  private String endTimeZone;

  @NotNull
  @Enumerated(EnumType.STRING)
  private InterviewFormat format;

  // Attendees

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_interview__talent__talent_id"))
  private Talent talent;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ParticipationStatus talentResponseStatus = ParticipationStatus.NEEDS_ACTION;

  private LocalDateTime talentRespondedAt;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_interview__employer__employer_id"))
  private Employer employer;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ParticipationStatus employerResponseStatus = ParticipationStatus.NEEDS_ACTION;

  private LocalDateTime employerRespondedAt;

  // ===============================================================================================

  public void handleTalentResponse(ParticipationStatus talentResponseStatus) {
    setTalentResponseStatus(talentResponseStatus);
    setTalentRespondedAt(LocalDateTime.now(Clock.systemUTC()));
    setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));
  }

  /**
   * In this case, the participation status has already been set by SDR
   */
  public void handleTalentResponseFromPreviousState() {
    ParticipationStatus previousTalentResponseStatus = getValueFromPreviousState(
        "talentResponseStatus",
        ParticipationStatus.class);
    if (!getTalentResponseStatus().equals(previousTalentResponseStatus)) {
      setTalentRespondedAt(LocalDateTime.now(Clock.systemUTC()));
      setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));
    }
  }

  public Interview handleEmployerResponse(ParticipationStatus employerResponseStatus) {
    setEmployerResponseStatus(employerResponseStatus);
    setEmployerRespondedAt(LocalDateTime.now(Clock.systemUTC()));
    setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));
    return this;
  }

  /**
   * Update the interview status
   *
   * @return {@code true} if status has been updated, {@code false} otherwise
   */
  public boolean updateStatus() {
    InterviewStatus previousStatus = getStatus();
    if (ParticipationStatus.ACCEPTED.equals(talentResponseStatus)
        && ParticipationStatus.ACCEPTED.equals(employerResponseStatus)) {
      setStatus(InterviewStatus.CONFIRMED);
    } else if (ParticipationStatus.DECLINED.equals(talentResponseStatus)
        || ParticipationStatus.DECLINED.equals(employerResponseStatus)) {
      setStatus(InterviewStatus.CANCELLED);
    } else {
      setStatus(InterviewStatus.TENTATIVE);
    }
    // status may or may not have been updated
    if (getStatus().equals(previousStatus)) {
      return false;
    } else {
      setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));
      return true;
    }
  }

  // GETTERS/SETTERS ===============================================================================

  public ZonedDateTime getStartAt() {
    if (Objects.isNull(startAt)) {
      startAt = getStartDateTime().withZoneSameInstant(ZoneId.of(getStartTimeZone()));
    }
    return startAt;
  }

  public void setStartAt(ZonedDateTime startAt) {
    this.startAt = startAt;
    setStartDateTime(startAt);
    setStartTimeZone(startAt.getZone().toString());
  }

  public ZonedDateTime getEndAt() {
    if (Objects.isNull(endAt)) {
      endAt = getEndDateTime().withZoneSameInstant(ZoneId.of(getEndTimeZone()));
    }
    return endAt;
  }

  public void setEndAt(ZonedDateTime endAt) {
    this.endAt = endAt;
    setEndDateTime(endAt);
    setEndTimeZone(endAt.getZone().toString());
  }
}
