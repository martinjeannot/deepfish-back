package com.deepfish.talent.services;

import com.deepfish.linkedin.domain.LiteProfile;
import com.deepfish.talent.domain.Talent;
import java.util.UUID;

/**
 * Service related to the {@link com.deepfish.talent.domain.Talent} domain object
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
   * Activate the given talent
   *
   * @param talent the talent to activate
   * @return the talent
   */
  Talent activate(Talent talent);

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
   * Sign a talent in from his LinkedIn lite profile
   *
   * @param liteProfile talent lite profile
   * @param emailAddress talent email address
   * @param utmId UTM parameters id
   * @return the signed in talent
   */
  Talent signInFromLinkedIn(LiteProfile liteProfile, String emailAddress, UUID utmId);

  /**
   * Sign a new talent up from his LinkedIn lite profile
   *
   * @param liteProfile talent lite profile
   * @param emailAddress talent email address
   * @param utmId UTM parameters id
   * @return the newly registered talent
   */
  Talent signUpFromLinkedIn(LiteProfile liteProfile, String emailAddress, UUID utmId);

  /**
   * Update the profile completeness percentage of the given talent
   *
   * @param talentId the id of the talent
   * @return the percentage value
   */
  float updateProfileCompleteness(UUID talentId);
}
