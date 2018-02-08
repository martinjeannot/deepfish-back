package com.deepfish.talent.domain.conditions;

import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {FixedLocation.class})
public interface DefaultFixedLocationsProjection {

  UUID getId();

  String getL10nKey();

  FixedLocation getParentLocation();
}
