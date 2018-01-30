package com.deepfish.employer.repositories;

import com.deepfish.employer.domain.Requirement;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RequirementRepository extends PagingAndSortingRepository<Requirement, UUID> {

}
