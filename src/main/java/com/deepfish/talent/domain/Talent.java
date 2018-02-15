package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.user.domain.AbstractUser;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
@Accessors(chain = true)
@ToString(callSuper = true, exclude = {"profile", "conditions"})
@EqualsAndHashCode(callSuper = true, exclude = {"profile", "conditions"})
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Talent extends AbstractUser {

  /**
   * We have both a linkedInId and an email property in case we need to switch the username property
   * from one to another
   */
  @NotBlank
  @Column(unique = true)
  @Setter(AccessLevel.NONE)
  private String linkedInId;

  @NotBlank
  private String email;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime registeredAt = LocalDateTime.now();

  @NotNull
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> profile;

  @NotNull
  @OneToOne(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Conditions conditions;

  @NotNull
  @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Opportunity> opportunities = new HashSet<>();

  @NotNull
  @Enumerated(EnumType.STRING)
  private TalentMaturityLevel maturityLevel;

  @NotNull
  @Column(columnDefinition = "text")
  private String selfPitch = "";

  // ===============================================================================================

  public Talent(String linkedInId) {
    this.linkedInId = linkedInId;
    setUsername(linkedInId);
  }

  // GETTERS & SETTERS =============================================================================

  public Talent setConditions(Conditions conditions) {
    this.conditions = conditions;
    if (conditions != null) {
      conditions.setTalent(this); // synchronization
    }
    return this;
  }
}
