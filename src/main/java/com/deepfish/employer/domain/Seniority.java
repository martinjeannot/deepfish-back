package com.deepfish.employer.domain;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
public class Seniority {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotBlank
  private String l10nKey;

  private int orderIndex;
}
