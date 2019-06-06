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
 * A client industry type refers to a group of companies operating in similar business lines (i.e.
 * who is it sold to)
 */
@Entity
@Data
public class ClientIndustryType {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotBlank
  private String l10nKey;

  private boolean enabled;

  private int orderIndex;
}
