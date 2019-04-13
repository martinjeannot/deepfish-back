package com.deepfish.talent.services;

import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.List;
import java.util.UUID;

/**
 * Service related to the {@link com.deepfish.talent.domain.opportunity.Opportunity} domain object
 */
public interface OpportunityService {

  /**
   * Decline all pending opportunities and deactivate the given talent
   *
   * @param talentId the id of the talent
   * @param bulkDeclinationReason the declination reason
   * @return the list of the company names of the declined opportunities
   */
  List<String> declineInBulk(UUID talentId, String bulkDeclinationReason);

  /**
   * Handle employer declination of the given opportunity
   *
   * @param declinedOpportunity the declined opportunity
   */
  void handleEmployerDeclination(Opportunity declinedOpportunity);
}
