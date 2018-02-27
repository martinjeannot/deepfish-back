package com.deepfish.talent.domain.qualification;

import com.deepfish.talent.domain.Talent;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
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

  private int complexSellingSkillsRating;

  private int huntingSkillsRating;

  private int technicalSkillsRating;
}
