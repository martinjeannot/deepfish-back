package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.conditions.Job;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface JobRepository extends PagingAndSortingRepository<Job, UUID> {

}
