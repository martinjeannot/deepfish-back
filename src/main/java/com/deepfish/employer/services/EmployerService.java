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
   */
  void create(Employer employer);

  /**
   * Sign a new employer up
   *
   * @param employer the employer to sign up
   */
  void signUp(Employer employer);
}
