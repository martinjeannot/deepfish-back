package com.deepfish.employer.repositories;

import com.deepfish.employer.domain.requirement.Seniority;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SeniorityRepository extends PagingAndSortingRepository<Seniority, UUID> {

}
