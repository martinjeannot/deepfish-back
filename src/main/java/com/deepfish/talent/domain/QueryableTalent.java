package com.deepfish.talent.domain;

import com.deepfish.company.domain.CompanyMaturityLevel;
import com.querydsl.core.annotations.QueryEntity;
import java.math.BigDecimal;
import java.util.Collection;
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
  private String searchQuery;

  @Transient
  private Collection<CompanyMaturityLevel> companyMaturityLevelsNotIn;

  @Transient
  private BigDecimal minFixedSalary;

  @Transient
  private BigDecimal maxFixedSalary;

  @Transient
  private int minYearsOfExperience;

  @Transient
  private int maxYearsOfExperience;
}
