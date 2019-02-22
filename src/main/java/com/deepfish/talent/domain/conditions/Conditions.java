package com.deepfish.talent.domain.conditions;

import com.deepfish.company.domain.CompanyMaturityLevel;
import com.deepfish.talent.domain.Talent;
import com.querydsl.core.annotations.QueryEntity;
import java.math.BigDecimal;
import java.time.Clock;
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
  private LocalDate canStartOn = LocalDate.now(Clock.systemUTC());

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_company_maturity_levels__conditions")),
      inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_company_maturity_levels__company_maturity_level")))
  private Set<CompanyMaturityLevel> companyMaturityLevels = new HashSet<>();

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_job_types__conditions")),
      inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_job_types__job_type")))
  private Set<JobType> jobTypes = new HashSet<>();

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_commodity_types__conditions")),
      inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_commodity_types__commodity_type")))
  private Set<CommodityType> commodityTypes = new HashSet<>();

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_task_types__conditions")),
      inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_task_types__task_type")))
  private Set<TaskType> taskTypes = new HashSet<>();

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_industry_types__conditions")),
      inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_industry_types__industry_type")))
  private Set<IndustryType> industryTypes = new HashSet<>();

  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_fixed_locations__conditions")),
      inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_conditions_fixed_locations__fixed_location")))
  private Set<FixedLocation> fixedLocations = new HashSet<>();
}
