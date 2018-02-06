package com.deepfish.talent.domain;

import org.mapstruct.ObjectFactory;

public class TalentFactory {

  @ObjectFactory
  public Talent createTalent(TalentProfile profile) {
    return new Talent(profile.getLinkedInId());
  }
}
