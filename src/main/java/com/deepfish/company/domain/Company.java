package com.deepfish.company.domain;

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
public class Company {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotBlank
  private String name;
}
