package com.deepfish.security.auth.linkedin;

import com.deepfish.talent.domain.Talent;
import java.util.Map;
import org.mapstruct.ObjectFactory;

public class TalentFactory {

  @ObjectFactory
  public Talent createTalent(Map<String, String> map) {
    return new Talent(map.get("id"));
  }
}
