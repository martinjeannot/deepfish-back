package com.deepfish.talent.domain.opportunity;

import java.time.Clock;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
public class OpportunityDatum {

  @Id
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now(Clock.systemUTC());

  private long totalCreated;

  private long createdVariation;

  private long totalTalentPending;

  private long talentPendingVariation;

  private long totalEmployerPending;

  private long employerPendingVariation;

  private long totalTalentAccepted;

  private long talentAcceptedVariation;

  private long totalEmployerAccepted;

  private long employerAcceptedVariation;

  private long totalTalentDeclined;

  private long talentDeclinedVariation;

  private long totalEmployerDeclined;

  private long employerDeclinedVariation;

  private long totalTalentExpired;

  private long talentExpiredVariation;

  public OpportunityDatum(long totalCreated, long totalTalentPending, long totalEmployerPending,
      long totalTalentAccepted, long totalEmployerAccepted, long totalTalentDeclined,
      long totalEmployerDeclined, long totalTalentExpired) {
    this.totalCreated = totalCreated;
    this.totalTalentPending = totalTalentPending;
    this.totalEmployerPending = totalEmployerPending;
    this.totalTalentAccepted = totalTalentAccepted;
    this.totalEmployerAccepted = totalEmployerAccepted;
    this.totalTalentDeclined = totalTalentDeclined;
    this.totalEmployerDeclined = totalEmployerDeclined;
    this.totalTalentExpired = totalTalentExpired;
  }

  public void calculateVariations(OpportunityDatum lastSample) {
    createdVariation = totalCreated - lastSample.getTotalCreated();
    talentPendingVariation = totalTalentPending - lastSample.getTotalTalentPending();
    employerPendingVariation = totalEmployerPending - lastSample.getTotalEmployerPending();
    talentAcceptedVariation = totalTalentAccepted - lastSample.getTotalTalentAccepted();
    employerAcceptedVariation = totalEmployerAccepted - lastSample.getTotalEmployerAccepted();
    talentDeclinedVariation = totalTalentDeclined - lastSample.getTotalTalentDeclined();
    employerDeclinedVariation = totalEmployerDeclined - lastSample.getTotalEmployerDeclined();
    talentExpiredVariation = totalTalentExpired - lastSample.getTotalTalentExpired();
  }
}
