package com.deepfish.talent.services;

import com.deepfish.talent.domain.Talent;
import java.util.Map;
import java.util.UUID;

/**
 * Service related to {@link Talent} entity
 */
public interface TalentService {

  /**
   * Create a new talent
   *
   * @param talent the talent to create
   * @return the newly created talent
   */
  Talent create(Talent talent);

  /**
   * @see TalentService#deactivate(Talent, String)
   */
  Talent deactivate(UUID talentId, String deactivationReason);

  /**
   * Deactivate the given talent
   *
   * @param talent the talent to deactivate
   * @param deactivationReason his deactivation reason
   * @return the talent
   */
  Talent deactivate(Talent talent, String deactivationReason);

  /**
   * Sign a talent in from his LinkedIn basic profile
   *
   * @param basicProfile a Map of data from LinkedIn
   * @return the signed in talent
   */
  Talent signInFromLinkedin(Map<String, Object> basicProfile);

  /**
   * Sign a new talent up from his LinkedIn basic profile
   *
   * @param basicProfile a Map of data from LinkedIn
   * @return the newly registered talent
   */
  Talent signUpFromLinkedIn(Map<String, Object> basicProfile);

  /**
   * Update the profile completeness percentage of the given talent
   *
   * @param talentId the id of the talent
   * @return the percentage value
   */
  float updateProfileCompleteness(UUID talentId);
}
