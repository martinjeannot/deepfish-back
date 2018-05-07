package com.deepfish.talent.services;

import java.util.UUID;

/**
 * Service related to {@link com.deepfish.talent.domain.opportunity.Opportunity} entity
 */
public interface OpportunityService {

  /**
   * Decline all pending opportunities and deactivate the given talent
   *
   * @param talentId the id of the talent
   */
  void declineInBulk(UUID talentId);
}
