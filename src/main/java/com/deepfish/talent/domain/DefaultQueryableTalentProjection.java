package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.DefaultConditionsProjection;
import com.deepfish.talent.domain.qualification.Qualification;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {QueryableTalent.class})
public interface DefaultQueryableTalentProjection {

  UUID getId();

  String getFirstName();

  String getLastName();

  LocalDateTime getCreatedAt();

  String getLinkedinPublicProfileUrl();

  String getProfilePictureUrl();

  Map<String, Object> getBasicProfile();

  int getYearsOfExperience();

  DefaultConditionsProjection getConditions();

  Qualification getQualification();

  TalentMaturityLevel getMaturityLevel();
}
