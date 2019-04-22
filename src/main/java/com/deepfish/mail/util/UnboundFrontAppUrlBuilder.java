package com.deepfish.mail.util;

import com.deepfish.security.auth.TokenFactory;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

/**
 * Unbound {@link FrontAppUrlBuilder}.
 *
 * It is "unbound" because usually operating in non-servlet context (batch for example), hence the
 * use of {@link org.springframework.web.util.UriComponentsBuilder} instead of the more web-focused
 * {@link org.springframework.web.servlet.support.ServletUriComponentsBuilder}.
 *
 * Since we still want the generated URLs to be useful as is, we need to manually bound some kind of
 * web context.
 */
@Component
public class UnboundFrontAppUrlBuilder implements FrontAppUrlBuilder {

  @Value("${deepfish.front.hash-mode}")
  private boolean isHashMode;

  @Value("#{'${deepfish.front.scheme}' + '://' + '${deepfish.front.host}' + ('${deepfish.front.port}'.isEmpty() ? '' : ':' + '${deepfish.front.port}' )}")
  private String frontAppUrl;

  private final String talentQualificationCalendlyUrl;

  private final TokenFactory tokenFactory;

  private final ObjectMapper objectMapper;

  public UnboundFrontAppUrlBuilder(
      @Value("${calendly.talent-qualification.url:#{null}}") String talentQualificationCalendlyUrl,
      TokenFactory tokenFactory,
      ObjectMapper objectMapper
  ) {
    this.talentQualificationCalendlyUrl = talentQualificationCalendlyUrl;
    this.tokenFactory = tokenFactory;
    this.objectMapper = objectMapper;
  }

  @Override
  public String getTalentDataManagementUrl(UUID talentId) {
    return UriComponentsBuilder
        .fromHttpUrl(frontAppUrl)
        .path(isHashMode ? "#" : "")
        .path("/admin/data-management/master/talents/{talentId}")
        .buildAndExpand(talentId)
        .toString();
  }

  public String getTalentOpportunityUrl(Opportunity opportunity) {
    OAuth2AccessToken authToken = tokenFactory.createToken(opportunity.getTalent());
    try {
      return UriComponentsBuilder
          .fromHttpUrl(frontAppUrl)
          .path(isHashMode ? "#" : "")
          .path("/talent/opportunities/{opportunityId}")
          .queryParam("auth_token",
              UriUtils.encode(objectMapper.writeValueAsString(authToken), "UTF-8"))
          .buildAndExpand(opportunity.getId())
          .toString();
    } catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getTalentQualificationCalendlyUrl(Talent talent) {
    return UriComponentsBuilder
        .fromHttpUrl(talentQualificationCalendlyUrl)
        .queryParam("name", talent.getFirstName() + " " + talent.getLastName())
        .queryParam("email", talent.getEmail())
        .queryParam("a1", talent.getPhoneNumber())
        .toUriString();
  }

  @Override
  public String getTalentProfilePictureUrl() {
    return UriComponentsBuilder
        .fromHttpUrl(frontAppUrl)
        .path("/static/img/avatar.png")
        .toUriString();
  }
}
