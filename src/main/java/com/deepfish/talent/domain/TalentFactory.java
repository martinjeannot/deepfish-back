package com.deepfish.talent.domain;

import com.deepfish.linkedin.domain.LiteProfile;
import java.util.Map;
import org.mapstruct.ObjectFactory;

public class TalentFactory {

  @ObjectFactory
  public Talent createTalent(LiteProfile liteProfile) {
    return new Talent(liteProfile.getId());
  }
}
