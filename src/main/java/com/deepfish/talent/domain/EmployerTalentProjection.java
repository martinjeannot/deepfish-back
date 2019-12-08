package com.deepfish.talent.domain;

import com.deepfish.talent.domain.conditions.EmployerConditionsProjection;
import com.deepfish.talent.domain.qualification.EmployerQualificationProjection;
import com.deepfish.user.domain.DefaultUserProjection;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employer", types = {Talent.class})
public interface EmployerTalentProjection {

  UUID getId();

  String getFirstName();

  String getLastName();

  // String getPhoneNumber();

  // String getEmail();

  // String getLinkedinPublicProfileUrl();

  String getProfilePictureUrl();

  Map<String, Object> getFullProfile();

  DefaultUserProjection getTalentAdvocate();

  int getYearsOfExperience();

  EmployerConditionsProjection getConditions();

  EmployerQualificationProjection getQualification();

  JobFunction getJobFunction();

  String getSelfPitch();
}
