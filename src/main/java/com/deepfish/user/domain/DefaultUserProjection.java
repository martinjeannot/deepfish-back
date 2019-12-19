package com.deepfish.user.domain;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {User.class})
public interface DefaultUserProjection {

  UUID getId();

  String getFirstName();

  String getLastName();

  String getPhoneNumber();

  @Value("#{target.username}")
  String getEmail();

  @Value("#{target.profilePictureUri != null ? @staticResourceResolver.resolve(target.profilePictureUri).toString() : null}")
  String getProfilePictureUrl();
}
