package com.deepfish.talent.domain;

import com.deepfish.user.domain.AbstractUser;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Talent extends AbstractUser {

  @NotBlank
  @Column(unique = true)
  @Setter(AccessLevel.NONE)
  private String linkedInId;

  public Talent(String linkedInId) {
    this.linkedInId = linkedInId;
  }
}
