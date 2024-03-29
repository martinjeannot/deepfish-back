package com.deepfish.talent.domain.conditions;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * An industry type refers to a group of similar products or services (i.e. what is being sold)
 */
@Entity
@Data
public class IndustryType {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotBlank
  private String l10nKey;

  private boolean enabled;

  private int orderIndex;
}
