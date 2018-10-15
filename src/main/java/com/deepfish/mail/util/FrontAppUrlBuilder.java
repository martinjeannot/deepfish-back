package com.deepfish.mail.util;

import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.UUID;

/**
 * This utility component aims at providing helper functions to create custom URL for our front app
 */
public interface FrontAppUrlBuilder {

  String getTalentDataManagementUrl(UUID talentId);

  String getTalentOpportunityUrl(Opportunity opportunity);
}
