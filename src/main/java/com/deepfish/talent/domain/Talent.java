package com.deepfish.talent.domain;

import com.deepfish.user.domain.AbstractUser;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Talent extends AbstractUser {

  @NotBlank
  @Column(unique = true)
  @Setter(AccessLevel.NONE)
  private String linkedInId;

  @NotBlank
  private String linkedInEmail;

  @NotBlank
  private String email;

  @NotNull
  @Enumerated(EnumType.STRING)
  private MaturityLevel maturityLevel;

  public Talent(String linkedInId) {
    this.linkedInId = linkedInId;
  }
}
