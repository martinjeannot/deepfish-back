package com.deepfish.company.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = {Company.class})
public interface DefaultCompanyProjection {

  UUID getId();

  LocalDateTime getCreatedAt();

  String getName();

  CompanyStatus getStatus();

  String getDescription();

  String getLogoURI();

  @Value("#{target.logoURI != null ? @staticResourceResolver.resolve(target.logoURI).toString() : null}")
  String getLogoURL();

  String getWebsiteUrl();

  String getSize();

  String getHeadquartersAddress();

  Map<String, Object> getHeadquartersGeocode();

  String getFoundedIn();

  String getRevenue();

  String getCustomerReferences();

  @Value("#{target.coverImageUri != null ? @staticResourceResolver.resolve(target.coverImageUri).toString() : null}")
  String getCoverImageUrl();

  @Value("#{target.topImageUri != null ? @staticResourceResolver.resolve(target.topImageUri).toString() : null}")
  String getTopImageUrl();

  @Value("#{target.bottomImageUri != null ? @staticResourceResolver.resolve(target.bottomImageUri).toString() : null}")
  String getBottomImageUrl();

  String getFacebookUrl();

  String getInstagramUrl();

  String getLinkedinUrl();

  String getTwitterUrl();

  String getYoutubeUrl();
}
