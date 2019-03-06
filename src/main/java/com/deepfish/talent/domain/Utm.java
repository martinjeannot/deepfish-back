package com.deepfish.talent.domain;

import com.querydsl.core.annotations.QueryEntity;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@QueryEntity
@Data
public class Utm {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now(Clock.systemUTC());

  @NotBlank
  private String source;

  private String medium;

  private String campaign;

  private String term;

  private String content;
}
