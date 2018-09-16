package com.deepfish.company.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Company.class})
public interface DefaultCompanyProjection {

  UUID getId();

  LocalDateTime getCreatedAt();

  String getName();

  String getDescription();

  String getLogoURI();

  @Value("#{target.logoURI != null ? @staticResourceResolver.resolve(target.logoURI).toString() : null}")
  String getLogoURL();
}
