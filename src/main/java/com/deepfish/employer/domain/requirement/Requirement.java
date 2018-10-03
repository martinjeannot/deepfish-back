package com.deepfish.employer.domain.requirement;

import com.deepfish.company.domain.Company;
import com.deepfish.talent.domain.conditions.JobType;
import com.querydsl.core.annotations.QueryEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@QueryEntity
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_requirement__company__company_id"))
  private Company company;

  @NotBlank
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  private RequirementStatus status = RequirementStatus.OPEN;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_requirement__job_type__job_type_id"))
  private JobType jobType;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_requirement__seniority__seniority_id"))
  private Seniority seniority;

  private String location;

  @NotNull
  @Min(0L)
  private BigDecimal fixedSalary = BigDecimal.ZERO;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> typeform;

  @NotNull
  @Column(columnDefinition = "text")
  private String opportunityPitch = "";

  @NotNull
  @Column(columnDefinition = "text")
  private String notes = "";
}
