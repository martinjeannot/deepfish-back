package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.qualification.Qualification;
import com.deepfish.user.domain.AbstractUser;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.NotBlank;

@MappedSuperclass
@Data
@Accessors(chain = true)
@ToString(callSuper = true, exclude = {"basicProfile", "conditions", "qualification",
    "opportunities"})
@EqualsAndHashCode(callSuper = true, exclude = {"basicProfile", "conditions", "qualification",
    "opportunities"})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AbstractTalent extends AbstractUser {

  /**
   * We have both a linkedInId and an email property in case we need to switch the username property
   * from one to another
   */
  @NotBlank
  //@Setter(AccessLevel.NONE) FIXME after migration
  private String linkedInId;

  @NotBlank
  private String email;

  @NotNull
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> basicProfile;

  @NotNull
  @Column(columnDefinition = "text")
  private String basicProfileText;

  @NotNull
  @Column(columnDefinition = "text")
  private String fullProfileText;

  private float profileCompleteness;

  @NotNull
  private LocalDateTime profileCompletenessLastCalculatedAt =
      LocalDateTime.now(Clock.systemUTC()).minusYears(1);

  @NotNull
  private LocalDateTime profileCompletenessLastUpdatedAt = LocalDateTime.now(Clock.systemUTC());

  private int yearsOfExperience;

  private int numberOfManagedConsultants;

  private int numberOfManagedProjects;

  @NotNull
  @OneToOne(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Conditions conditions;

  @NotNull
  @OneToOne(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Qualification qualification;

  @NotNull
  @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Opportunity> opportunities = new HashSet<>();

  private boolean active;

  @Enumerated(EnumType.STRING)
  private TalentMaturityLevel maturityLevel;

  @NotNull
  @Column(columnDefinition = "text")
  private String selfPitch = "";

  @NotNull
  @Column(columnDefinition = "text")
  private String notes = "";
}
