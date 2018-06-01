package com.deepfish.employer.repositories;

import com.deepfish.employer.domain.Employer;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EmployerRepository extends PagingAndSortingRepository<Employer, UUID> {

  Employer findByUsername(String username);

  Page<Employer> findByUsernameContainingOrFirstNameContainingOrLastNameContainingOrCompanyNameContainingAllIgnoreCase(
      @Param("username") String username,
      @Param("firstName") String firstName,
      @Param("lastName") String lastName,
      @Param("companyName") String companyName,
      Pageable pageable);
}
