package com.deepfish.company.event;

import com.deepfish.company.domain.Company;
import com.deepfish.geo.geocoding.GeocodingService;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class CompanyEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyEventHandler.class);

  private final GeocodingService geocodingService;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public CompanyEventHandler(
      GeocodingService geocodingService,
      MailService mailService,
      MailFactory mailFactory
  ) {
    this.geocodingService = geocodingService;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @HandleBeforeSave
  public void onBeforeSave(Company company) {
    if (Objects.isNull(company.getHeadquartersGeocode())) {
      try {
        Map<String, Object> headquartersGeocode = geocodingService
            .geocode(company.getHeadquartersAddress(), Map.class);
        company.setHeadquartersGeocode(headquartersGeocode);
      } catch (RuntimeException e) {
        mailService.send(mailFactory.getAdminErrorMail(e));
      }
    }
  }
}
