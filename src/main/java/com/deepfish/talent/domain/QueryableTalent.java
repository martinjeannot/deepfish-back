package com.deepfish.talent.domain;

import com.deepfish.company.domain.CompanyMaturityLevel;
import com.querydsl.core.annotations.QueryEntity;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
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
  private Collection<CompanyMaturityLevel> companyMaturityLevelsIn;

  @Transient
  private List<CompanyMaturityLevel> companyMaturityLevelsNotIn;

  @Transient
  private BigDecimal minFixedSalary;

  @Transient
  private BigDecimal maxFixedSalary;
}
