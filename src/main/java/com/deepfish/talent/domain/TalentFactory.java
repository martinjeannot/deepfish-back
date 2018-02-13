package com.deepfish.talent.domain;

import java.util.Map;
import org.mapstruct.ObjectFactory;

public class TalentFactory {

  @ObjectFactory
  public Talent createTalent(Map map) {
    return new Talent(String.valueOf(map.get("id")));
  }
}
