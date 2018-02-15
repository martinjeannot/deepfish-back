package com.deepfish.talent.domain.opportunity;

import com.deepfish.employer.domain.Requirement;
import com.deepfish.talent.domain.Talent;
import com.deepfish.user.domain.User;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class Opportunity {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now();

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private User creator;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Talent talent;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Requirement requirement;

  @NotNull
  @Enumerated(EnumType.STRING)
  private OpportunityStatus status = OpportunityStatus.PENDING;

  @NotNull
  @Column(columnDefinition = "text")
  private String pitch = "";

  @NotNull
  @Column(columnDefinition = "text")
  private String refusalReason = "";
}
