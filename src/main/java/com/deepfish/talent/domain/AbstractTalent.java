package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.qualification.Qualification;
import com.deepfish.user.domain.AbstractUser;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

/**
 * @see <a href="https://github.com/vladmihalcea/hibernate-types/issues/30#issuecomment-386609972">
 * json vs jsonb</a>
 * @see <a href="https://stackoverflow.com/questions/15974474/mapping-postgresql-json-column-to-hibernate-value-type/37946530#37946530">
 * mapping postgresql json column</a>
 */
@MappedSuperclass
@Data
@Accessors(chain = true)
@ToString(callSuper = true, exclude = {"basicProfile", "conditions", "qualification",
    "opportunities"})
@EqualsAndHashCode(callSuper = true, exclude = {"basicProfile", "conditions", "qualification",
    "opportunities"})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AbstractTalent extends AbstractUser {

  @NotBlank
  private String email;

  /**
   * We have both a linkedIn id and an email property in case we need to switch the username
   * property from one to another
   */
  @NotBlank
  //@Setter(AccessLevel.NONE) FIXME after migration
  private String linkedinId;

  private String linkedinPublicProfileUrl;

  @NotBlank
  private String profilePictureUrl;

  /**
   * LinkedIn lite profile (as json) retrieved from LinkedIn API V2
   */
  @Type(type = "jsonb")
  @Column(columnDefinition = "json")
  private String liteProfileText;

  /**
   * LinkedIn basic profile (as jsonb) retrieved from LinkedIn API V1
   */
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> basicProfile;

  /**
   * LinkedIn basic profile (as json) retrieved from LinkedIn API V1
   */
  @Type(type = "jsonb")
  @Column(columnDefinition = "json")
  private String basicProfileText;

  /**
   * LinkedIn full profile (as jsonb) retrieved by scraping
   */
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> fullProfile;

  /**
   * LinkedIn full profile (as text) retrieved by hand
   */
  @NotNull
  @Column(columnDefinition = "text")
  private String fullProfileText = "";

  private LocalDateTime linkedinProfileLastRetrievedAt;

  private LocalDateTime linkedinProfileLastRetrievalAttemptedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_talent__utm__utm_id"))
  private Utm utm;

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

  private LocalDate reactivatedOn;

  @Enumerated(EnumType.STRING)
  private TalentMaturityLevel maturityLevel;

  @NotNull
  @Column(columnDefinition = "text")
  private String selfPitch = "";

  @NotNull
  @Column(columnDefinition = "text")
  private String notes = "";
}
