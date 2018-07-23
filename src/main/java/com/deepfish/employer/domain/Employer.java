package com.deepfish.employer.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.user.domain.AbstractUser;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UK_employer__username", columnNames = {"username"}),
})
@Data
@EqualsAndHashCode(callSuper = true, exclude = "company")
@ToString(callSuper = true, exclude = {"company"})
public class Employer extends AbstractUser {

  @NotNull
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_employer__company__company_id"))
  private Company company;

  @NotNull
  @Column(columnDefinition = "text")
  private String notes = "";
}
