package com.deepfish.employer.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.user.domain.AbstractUser;
import com.querydsl.core.annotations.QueryEntity;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@QueryEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class Employer extends AbstractUser {

  @NotNull
  @ManyToOne(cascade = CascadeType.ALL)
  private Company company;
}
