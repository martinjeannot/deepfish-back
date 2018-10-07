package com.deepfish.employer.repositories;

import com.deepfish.company.domain.Company;
import com.deepfish.employer.domain.requirement.Requirement;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RequirementRepository extends PagingAndSortingRepository<Requirement, UUID>,
    QueryDslPredicateExecutor<Requirement> {

  List<Requirement> findByCompany(@Param("company") Company company);

  Page<Requirement> findByNameContainingOrCompanyNameContainingAllIgnoreCase(
      @Param("name") String name,
      @Param("companyName") String companyName,
      Pageable pageable);
}
