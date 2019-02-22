package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.qualification.Qualification;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.querydsl.core.annotations.QueryEntity;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UK_talent__username", columnNames = {"username"}),
    @UniqueConstraint(name = "UK_talent__linkedin_id", columnNames = {"linkedinId"}),
})
@QueryEntity
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Talent extends AbstractTalent {

  public Talent(String linkedinId) {
    setLinkedinId(linkedinId);
    setUsername(linkedinId);
  }

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  private Map<String, Object> previousState;

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  private String deactivationReason;

  public void activate() {
    setActive(true);
  }

  public void deactivate() {
    setActive(false);
  }

  public void calculateProfileCompleteness() {
    float profileCompleteness = 0;
    // Phone number
    if (!getPhoneNumber().equals("null")) {
      profileCompleteness += 10;
    }
    // Maturity level
    if (Objects.nonNull(getMaturityLevel())) {
      profileCompleteness += 10;
    }
    // Fixed salary
    if (getConditions().getFixedSalary().compareTo(BigDecimal.ZERO) > 0) {
      profileCompleteness += 40;
    }
    // Company maturity levels
    if (!getConditions().getCompanyMaturityLevels().isEmpty()) {
      profileCompleteness += 5;
    }
    // Commodity types
    if (!getConditions().getCommodityTypes().isEmpty()) {
      profileCompleteness += 5;
    }
    // Task types
    if (!getConditions().getTaskTypes().isEmpty()) {
      profileCompleteness += 5;
    }
    // Industry types
    if (!getConditions().getIndustryTypes().isEmpty()) {
      profileCompleteness += 5;
    }
    // Fixed locations
    if (!getConditions().getFixedLocations().isEmpty()) {
      profileCompleteness += 5;
    }
    // Years of experience
    if (getYearsOfExperience() > 0) {
      profileCompleteness += 5;
    }
    // Number of managed consultants
    if (getNumberOfManagedConsultants() > 0) {
      profileCompleteness += 5;
    }
    // Number of managed projects
    if (getNumberOfManagedProjects() > 0) {
      profileCompleteness += 3;
    }
    // Self pitch
    if (!getSelfPitch().isEmpty()) {
      profileCompleteness += 2;
    }

    setProfileCompletenessLastCalculatedAt(LocalDateTime.now(Clock.systemUTC()));
    if (getProfileCompleteness() != profileCompleteness) {
      setProfileCompleteness(profileCompleteness);
      setProfileCompletenessLastUpdatedAt(getProfileCompletenessLastCalculatedAt());
    }
  }

  // GETTERS & SETTERS =============================================================================

  public Talent setConditions(Conditions conditions) {
    super.setConditions(conditions);
    if (conditions != null) {
      conditions.setTalent(this); // synchronization
    }
    return this;
  }

  public Talent setQualification(Qualification qualification) {
    super.setQualification(qualification);
    if (qualification != null) {
      qualification.setTalent(this); // synchronization
    }
    return this;
  }
}
