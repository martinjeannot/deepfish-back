package com.deepfish.talent.domain;

import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {EmployerQueryableTalent.class})
public interface DefaultEmployerQueryableTalentProjection {

  UUID getId();

  String getFirstName();

  String getLastName();
}
