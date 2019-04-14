package com.deepfish.core.domain;

import com.deepfish.interview.domain.ParticipationStatus;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface StateRetaining {

  Map<Class, TypeHandler> typeHandlers = new HashMap<Class, TypeHandler>() {{
    put(OpportunityStatus.class, value -> OpportunityStatus.valueOf(value.toString()));
    put(ParticipationStatus.class, value -> ParticipationStatus.valueOf(value.toString()));
  }};

  Map<String, Object> getPreviousState();

  default <V> V getValueFromPreviousState(String key, Class<V> type) {
    if (getPreviousState().containsKey(key) && Objects.nonNull(getPreviousState().get(key))) {
      return (V) typeHandlers.get(type).handle(getPreviousState().get(key));
    }
    return null;
  }

  interface TypeHandler {

    Object handle(Object value);
  }
}
