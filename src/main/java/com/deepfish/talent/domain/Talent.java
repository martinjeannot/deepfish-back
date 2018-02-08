package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.profile.TalentProfile;
import com.deepfish.user.domain.AbstractUser;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
@Accessors(chain = true)
@ToString(callSuper = true, exclude = {"profile", "conditions"})
@EqualsAndHashCode(callSuper = true, exclude = {"profile", "conditions"})
@NoArgsConstructor
public class Talent extends AbstractUser {

  @NotBlank
  @Column(unique = true)
  @Setter(AccessLevel.NONE)
  private String linkedInId;

  @NotBlank
  private String email;

  @NotNull
  @OneToOne(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private TalentProfile profile;

  @NotNull
  @Enumerated(EnumType.STRING)
  private MaturityLevel maturityLevel;

  @NotNull
  @Column(columnDefinition = "TEXT")
  private String selfPitch = "";

  @NotNull
  @OneToOne(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Conditions conditions;

  public Talent(String linkedInId) {
    this.linkedInId = linkedInId;
  }

  // GETTERS & SETTERS =============================================================================

  public Talent setProfile(TalentProfile profile) {
    this.profile = profile;
    if (profile != null) {
      profile.setTalent(this); // synchronization
    }
    return this;
  }

  public Talent setConditions(Conditions conditions) {
    this.conditions = conditions;
    if (conditions != null) {
      conditions.setTalent(this); // synchronization
    }
    return this;
  }
}
