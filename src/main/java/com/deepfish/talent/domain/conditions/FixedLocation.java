package com.deepfish.talent.domain.conditions;

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
@Data
public class FixedLocation {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotBlank
  private String l10nKey;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_fixed_location__fixed_location__parent_location_id"))
  private FixedLocation parentLocation;
}
