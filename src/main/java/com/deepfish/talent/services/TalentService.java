package com.deepfish.talent.services;

import com.deepfish.talent.domain.Talent;
import java.util.Map;

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
   * Sign a new talent up from his LinkedIn profile
   *
   * @param profile a Map of data from LinkedIn
   * @return the newly registered talent
   */
  Talent signUpFromLinkedIn(Map<String, Object> profile);
}
