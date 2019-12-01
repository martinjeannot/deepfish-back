package com.deepfish.user.domain;

import com.deepfish.employer.domain.Employer;
import com.deepfish.talent.domain.Talent;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "Users", uniqueConstraints = {
    @UniqueConstraint(name = "UK_users__username", columnNames = {"username"}),
})
@Data
@ToString(callSuper = true, exclude = {"talents", "employers"})
@EqualsAndHashCode(callSuper = true, exclude = {"talents", "employers"})
public class User extends AbstractUser {

  @NotNull
  @OneToMany(mappedBy = "talentAdvocate")
  private Set<Talent> talents = new HashSet<>();

  @NotNull
  @OneToMany(mappedBy = "clientExecutive")
  private Set<Employer> employers = new HashSet<>();
}
