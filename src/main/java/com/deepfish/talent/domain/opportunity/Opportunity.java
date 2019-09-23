package com.deepfish.talent.domain.opportunity;

import com.deepfish.core.domain.StateRetaining;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.interview.domain.Interview;
import com.deepfish.talent.domain.Talent;
import com.deepfish.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.querydsl.core.annotations.QueryEntity;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Entity
@QueryEntity
@Data
@ToString(exclude = {"creator", "talent", "requirement"})
@EqualsAndHashCode(exclude = {"creator", "talent", "requirement"})
public class Opportunity implements StateRetaining {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  private Map<String, Object> previousState;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now(Clock.systemUTC());

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_opportunity__user__creator_id"))
  private User creator;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_opportunity__talent__talent_id"))
  private Talent talent;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_opportunity__requirement__requirement_id"))
  private Requirement requirement;

  @NotNull
  @OneToMany(mappedBy = "opportunity")
  private Set<Interview> interviews = new HashSet<>();

  @NotNull
  @Column(columnDefinition = "text")
  private String pitch = "";

  @NotNull
  @Enumerated(EnumType.STRING)
  private OpportunityStatus talentStatus = OpportunityStatus.PENDING;

  private LocalDateTime talentRespondedAt;

  @NotNull
  @Column(columnDefinition = "text")
  private String talentDeclinationReason = "";

  private boolean declinedInBulk;

  private boolean forwarded;

  private LocalDateTime forwardedAt;

  private boolean forwardedOnce; // TODO remove this after forwardedAt "mean retrieval"

  @Enumerated(EnumType.STRING)
  private OpportunityStatus employerStatus;

  private LocalDateTime employerRespondedAt;

  private LocalDateTime employerAcceptedAt;

  private LocalDateTime employerDeclinedAt;

  @NotNull
  @Column(columnDefinition = "text")
  private String employerDeclinationReason = "";

  private LocalDate talentStartedOn;

  private LocalDate trialPeriodTerminatedOn; // used to computed trial-to-hire rate

  // ===============================================================================================

  /**
   * Tag this opportunity as forwarded to the related employer (through requirement)
   */
  public void forwardToEmployer() {
    setForwarded(true);
    setForwardedAt(LocalDateTime.now(Clock.systemUTC()));
    setForwardedOnce(true);
  }

  /**
   * Untag this opportunity as forwarded to the related employer (through requirement)
   */
  public void retrieveFromEmployer() {
    setForwarded(false);
    // clean previous employer response
    setEmployerStatus(null);
  }

  public void handleTalentResponse(
      OpportunityStatus talentStatus,
      String talentDeclinationReason,
      boolean declinedInBulk
  ) {
    setTalentStatus(talentStatus);
    setTalentRespondedAt(LocalDateTime.now(Clock.systemUTC()));
    if (OpportunityStatus.DECLINED.equals(talentStatus)) {
      setTalentDeclinationReason(talentDeclinationReason);
      setDeclinedInBulk(declinedInBulk);
    }
  }

  public void handleEmployerResponse(
      OpportunityStatus employerStatus,
      String employerDeclinationReason
  ) {
    setEmployerStatus(employerStatus);
    LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    setEmployerRespondedAt(now);
    switch (employerStatus) {
      case ACCEPTED:
        setEmployerAcceptedAt(now);
        break;
      case DECLINED:
        setEmployerDeclinedAt(now);
        setEmployerDeclinationReason(employerDeclinationReason);
        break;
      default:
        throw new IllegalArgumentException("Unknown employer status : " + employerStatus);
    }
  }

  // GETTERS/SETTERS ===============================================================================

  @JsonIgnore
  public OpportunityStatus getPreviousTalentStatus() {
    return getValueFromPreviousState("talentStatus", OpportunityStatus.class);
  }

  @JsonIgnore
  public OpportunityStatus getPreviousEmployerStatus() {
    return getValueFromPreviousState("employerStatus", OpportunityStatus.class);
  }
}
