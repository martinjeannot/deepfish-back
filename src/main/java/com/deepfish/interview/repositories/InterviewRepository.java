package com.deepfish.interview.repositories;

import com.deepfish.interview.domain.Interview;
import java.util.List;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface InterviewRepository extends PagingAndSortingRepository<Interview, UUID>,
    QueryDslPredicateExecutor<Interview> {

  List<Interview> findBySharedId(UUID sharedId);
}
