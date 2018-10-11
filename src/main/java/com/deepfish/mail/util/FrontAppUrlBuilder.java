package com.deepfish.mail.util;

import java.util.UUID;

/**
 * This utility component aims at providing helper functions to create custom URL for our front app
 */
public interface FrontAppUrlBuilder {

  String getTalentDataManagementUrl(UUID talentId);
}
