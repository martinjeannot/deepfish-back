package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;

/**
 * Service related to {@link Employer} entity
 */
public interface EmployerService {

  /**
   * Create a new employer
   *
   * @param employer the employer to create
   * @return the newly created employer
   */
  Employer create(Employer employer);

  /**
   * Sign a new employer up
   *
   * @param employer the employer to sign up
   * @return the newly registered employer
   */
  Employer signUp(Employer employer);

  /**
   * Reset the password of the given employer's mail and send it by mail
   *
   * @param email the mail address of the user
   * @return true if the password was reset, false otherwise
   */
  boolean resetPassword(String email);
}
