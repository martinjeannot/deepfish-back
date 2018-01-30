package com.deepfish.company.repositories;

import com.deepfish.company.domain.Company;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CompanyRepository extends PagingAndSortingRepository<Company, UUID> {

}
