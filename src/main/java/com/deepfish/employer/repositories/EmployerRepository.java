package com.deepfish.employer.repositories;

import com.deepfish.employer.domain.Employer;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EmployerRepository extends PagingAndSortingRepository<Employer, UUID> {

  Employer findByUsername(String username);
}
