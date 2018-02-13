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
   */
  void create(Talent talent);

  /**
   * Sign a new talent up from his LinkedIn profile
   *
   * @param profile a Map of data from LinkedIn
   */
  void signUpFromLinkedIn(Map<String, Object> profile);
}
