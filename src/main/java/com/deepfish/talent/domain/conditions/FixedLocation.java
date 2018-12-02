package com.deepfish.talent.domain.conditions;

import com.querydsl.core.annotations.QueryEntity;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@QueryEntity
@Data
public class FixedLocation {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotBlank
  private String l10nKey;

  private boolean enabled;

  private int orderIndex;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_fixed_location__fixed_location__parent_location_id"))
  private FixedLocation parentLocation;
}
