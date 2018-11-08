package com.deepfish.talent.domain.opportunity;

import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.talent.domain.Talent;
import com.deepfish.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.querydsl.core.annotations.QueryEntity;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
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

@Entity
@QueryEntity
@Data
@ToString(exclude = {"creator", "talent", "requirement"})
@EqualsAndHashCode(exclude = {"creator", "talent", "requirement"})
public class Opportunity {

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
  @Column(columnDefinition = "text")
  private String pitch = "";

  @NotNull
  @Enumerated(EnumType.STRING)
  private OpportunityStatus talentStatus = OpportunityStatus.PENDING;

  @NotNull
  @Column(columnDefinition = "text")
  private String talentDeclinationReason = "";

  private LocalDateTime talentRespondedAt;

  private boolean forwarded;

  private LocalDateTime forwardedAt;

  private boolean forwardedOnce;

  @Enumerated(EnumType.STRING)
  private OpportunityStatus employerStatus;

  @NotNull
  @Column(columnDefinition = "text")
  private String employerDeclinationReason = "";

  private LocalDateTime employerRespondedAt;

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
    setForwardedAt(null);
  }

  public void handleTalentResponse(OpportunityStatus talentStatus, String talentDeclinationReason,
      boolean bulkDeclined) {
    setTalentStatus(talentStatus);
    setTalentRespondedAt(LocalDateTime.now(Clock.systemUTC()));
    if (OpportunityStatus.DECLINED.equals(talentStatus)) {
      setTalentDeclinationReason(talentDeclinationReason);
      // todo set bulkDeclined
    }
  }
}
