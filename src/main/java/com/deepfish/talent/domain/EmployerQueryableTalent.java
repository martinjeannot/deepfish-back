package com.deepfish.talent.domain;

import com.querydsl.core.annotations.QueryEntity;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Immutable;

@Entity
@QueryEntity
@Table(name = "talent")
@Immutable
public class EmployerQueryableTalent extends AbstractTalent {

  @Transient
  private UUID employerId;

  @Transient
  private Integer minExperience;

  @Transient
  private Integer maxExperience;

  @Transient
  private Integer minBaseSalary;

  @Transient
  private Integer maxBaseSalary;
}
