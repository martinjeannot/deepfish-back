package com.deepfish.employer.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.user.domain.AbstractUser;
import com.deepfish.user.domain.User;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@ToString(callSuper = true, exclude = {"company", "clientExecutive"})
@EqualsAndHashCode(callSuper = true, exclude = {"company", "clientExecutive"})
public class Employer extends AbstractUser {

  @NotNull
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_employer__company__company_id"))
  private Company company;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_employer__users__client_executive_id"))
  private User clientExecutive;

  @NotNull
  @Column(columnDefinition = "text")
  private String notes = "";

  // GETTERS/SETTERS ===============================================================================

  /**
   * Needed to prevent fetching a {@literal null} entity resulting in a 404 error from SDR (which
   * needs to be caught frontend-side)
   *
   * @return {@code true} if the employer has been assigned a client exec, {@code false} otherwise
   */
  public boolean gethasClientExecutive() {
    return clientExecutive != null;
  }
}
