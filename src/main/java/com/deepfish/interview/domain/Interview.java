package com.deepfish.interview.domain;

import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.querydsl.core.annotations.QueryEntity;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * See https://tools.ietf.org/html/rfc5545
 */
@Entity
@QueryEntity
@Data
@ToString(exclude = {"opportunity", "talent", "employer"})
@EqualsAndHashCode(exclude = {"opportunity", "talent", "employer"})
public class Interview {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  /**
   * Id shared between all slots of the same interview scheduling attempt
   */
  @NotNull
  private UUID sharedId;

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

  @NotNull
  private ZonedDateTime startAt;

  @NotNull
  private ZonedDateTime endAt;

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
}
