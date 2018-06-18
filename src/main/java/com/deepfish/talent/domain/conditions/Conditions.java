package com.deepfish.talent.domain.conditions;

import com.deepfish.company.domain.CompanyMaturityLevel;
import com.deepfish.talent.domain.Talent;
import com.querydsl.core.annotations.QueryEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Entity
@QueryEntity
@Data
@ToString(exclude = {"talent"})
@EqualsAndHashCode(exclude = {"talent"})
public class Conditions {

  @Id
  @Setter(AccessLevel.NONE)
  private UUID id;

  @MapsId
  @OneToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions__talent__talent_id"))
  private Talent talent;

  @NotNull
  @Min(0L)
  private BigDecimal fixedSalary = BigDecimal.ZERO;

  @NotNull
  private LocalDate canStartOn = LocalDate.now();

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_company_maturity_levels__conditions__conditions_talent_id")),
      inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_company_maturity_levels__company_maturity_level__company_maturity_levels_id")))
  private Set<CompanyMaturityLevel> companyMaturityLevels = new HashSet<>();

  @ManyToMany
  private Set<JobType> jobTypes = new HashSet<>();

  @ManyToMany
  private Set<CommodityType> commodityTypes = new HashSet<>();

  @ManyToMany
  private Set<TaskType> taskTypes = new HashSet<>();

  @ManyToMany
  private Set<FixedLocation> fixedLocations = new HashSet<>();
}
