package com.deepfish.employer.repositories;

import com.deepfish.company.domain.Company;
import com.deepfish.employer.domain.Requirement;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RequirementRepository extends PagingAndSortingRepository<Requirement, UUID> {

  List<Requirement> findByCompany(@Param("company") Company company);
}
