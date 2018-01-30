package com.deepfish.talent.domain;

import com.deepfish.company.domain.CompanyMaturityLevel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Conditions {

  @Id
  private UUID id;

  @OneToOne
  @MapsId
  private Talent talent;

  @NotNull
  @Min(0L)
  private BigDecimal fixedSalary = BigDecimal.ZERO;

  @NotNull
  private LocalDate canStartOn = LocalDate.now();

  @ManyToMany
  private Set<CompanyMaturityLevel> companyMaturityLevels = new HashSet<>();

  @ManyToMany
  private Set<Job> jobs = new HashSet<>();

  @ManyToMany
  private Set<CommodityType> commodityTypes = new HashSet<>();

  @ManyToMany
  private Set<TaskType> taskTypes = new HashSet<>();

  @ManyToMany
  private Set<FixedLocation> fixedLocations = new HashSet<>();
}
