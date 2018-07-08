package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.qualification.Qualification;
import com.querydsl.core.annotations.QueryEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UK_talent__username", columnNames = {"username"}),
    @UniqueConstraint(name = "UK_talent__linked_in_id", columnNames = {"linkedInId"}),
})
@QueryEntity
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Talent extends AbstractTalent {

  public Talent(String linkedInId) {
    setLinkedInId(linkedInId);
    setUsername(linkedInId);
  }

  public void activate() {
    setActive(true);
  }

  public void deactivate() {
    setActive(false);
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
