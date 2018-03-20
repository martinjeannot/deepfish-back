package com.deepfish.company.repositories;

import com.deepfish.company.domain.Company;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CompanyRepository extends PagingAndSortingRepository<Company, UUID> {

  Page<Company> findByNameContaining(@Param("name") String name, Pageable pageable);
}
