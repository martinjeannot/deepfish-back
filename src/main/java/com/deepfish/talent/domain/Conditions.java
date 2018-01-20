package com.deepfish.talent.domain;

import com.deepfish.company.domain.CompanyMaturityLevel;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.Data;

@Entity(name = "TalentConditions")
@Data
public class Conditions {

  @Id
  private UUID id;

  @OneToOne
  @MapsId
  private Talent talent;

  private double salary = 125000.89;

  @ManyToMany
  private Set<CompanyMaturityLevel> companyMaturityLevels = new HashSet<>();

  @ManyToMany
  private Set<Job> jobs = new HashSet<>();

  @ManyToMany
  private Set<CommodityType> commodityTypes = new HashSet<>();

  @ManyToMany
  private Set<TaskType> taskTypes = new HashSet<>();
}
