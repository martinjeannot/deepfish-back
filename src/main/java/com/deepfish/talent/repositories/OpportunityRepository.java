package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.opportunity.Opportunity;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OpportunityRepository extends PagingAndSortingRepository<Opportunity, UUID>,
    QueryDslPredicateExecutor<Opportunity> {

}
