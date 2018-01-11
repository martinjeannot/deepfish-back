package com.deepfish.talent.services;

import com.deepfish.talent.domain.Talent;

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
   * Sign a new talent up
   *
   * @param talent the talent to sign up
   */
  void signUp(Talent talent);
}
