package com.deepfish.talent.event;

import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.services.TalentService;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class TalentEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(TalentEventHandler.class);

  private final TalentService talentService;

  public TalentEventHandler(TalentService talentService) {
    this.talentService = talentService;
  }

  @HandleAfterSave
  public void onAfterSave(Talent talent) {
    if (Objects.nonNull(talent.getPreviousState())) {
      // [previous state] data gathering

      boolean previousActive = talent.isActive();
      if (talent.getPreviousState().containsKey("active")) {
        previousActive = Boolean.valueOf(talent.getPreviousState().get("active").toString());
      }

      // [previous state] data specific behavior

      if (talent.isActive() != previousActive) {
        if (talent.isActive()) {
          talentService.activate(talent);
        }
        if (!talent.isActive() && Objects.nonNull(talent.getDeactivationReason())) {
          talentService.deactivate(talent, talent.getDeactivationReason());
        }
      }
    }
  }
}
