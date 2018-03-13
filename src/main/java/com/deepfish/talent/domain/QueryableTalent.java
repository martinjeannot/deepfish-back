package com.deepfish.talent.domain;

import com.querydsl.core.annotations.QueryEntity;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Immutable;

@Entity
@QueryEntity
@Table(name = "talent")
@Immutable
public class QueryableTalent extends AbstractTalent {

  @Transient
  private BigDecimal minFixedSalary;

  @Transient
  private BigDecimal maxFixedSalary;
}
