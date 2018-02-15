package com.deepfish.employer.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.talent.domain.conditions.Job;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
public class Requirement {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now();

  @NotNull
  private UUID createdBy;

  @NotNull
  @ManyToOne
  private Company company;

  @NotNull
  private String name;

  @NotNull
  @ManyToOne
  private Job job;

  @NotNull
  @ManyToOne
  private Seniority seniority;

  @NotBlank
  private String location;

  @NotNull
  @Min(0L)
  private BigDecimal fixedSalary = BigDecimal.ZERO;

  @NotNull
  @Column(columnDefinition = "text")
  private String opportunityPitch = "";
}
