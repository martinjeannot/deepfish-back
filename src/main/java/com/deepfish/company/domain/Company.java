package com.deepfish.company.domain;

import com.deepfish.employer.domain.Employer;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
public class Company {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now();

  @NotBlank
  private String name;

  @NotNull
  @OneToMany(mappedBy = "company")
  private Set<Employer> employers = new HashSet<>();

  @NotNull
  @Column(columnDefinition = "text")
  private String description = "";
}
