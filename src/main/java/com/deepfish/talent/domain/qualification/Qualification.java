package com.deepfish.talent.domain.qualification;

import com.deepfish.talent.domain.Talent;
import com.querydsl.core.annotations.QueryEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@QueryEntity
@Data
@Accessors(chain = true)
@ToString(exclude = {"talent"})
@EqualsAndHashCode(exclude = {"talent"})
public class Qualification {

  @Id
  @Setter(AccessLevel.NONE)
  private UUID id;

  @MapsId
  @OneToOne
  private Talent talent;

  private int ranking;

  private int complexSellingSkillsRating;

  private int huntingSkillsRating;

  private int technicalSkillsRating;

  @NotNull
  @Column(columnDefinition = "text")
  private String recommendation = "";
}
