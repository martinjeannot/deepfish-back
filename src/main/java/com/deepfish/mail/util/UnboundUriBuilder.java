package com.deepfish.mail.util;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This utility class aims at providing helper functions to create custom URIs. It is "unbound"
 * because usually operating in non-servlet context (batch for example), hence the use of {@link
 * org.springframework.web.util.UriComponentsBuilder} instead of the more web-focused {@link
 * org.springframework.web.servlet.support.ServletUriComponentsBuilder}.
 *
 * Since we still want the generated URIs to be useful as is, we need to manually bound some kind of
 * web context.
 */
@Component
public class UnboundUriBuilder {

  @Value("${deepfish.front.hash-mode}")
  private boolean isHashMode;

  @Value("#{'${deepfish.front.scheme}' + '://' + '${deepfish.front.host}' + ('${deepfish.front.port}'.isEmpty() ? '' : ':' + '${deepfish.front.port}' )}")
  private String frontAppUrl;

  public String getTalentDataManagementUri(UUID talentId) {
    return UriComponentsBuilder.fromHttpUrl(frontAppUrl).path(isHashMode ? "#" : "")
        .path("/admin/data-management/master/talents/{talentId}").buildAndExpand(talentId)
        .toString();
  }
}
