package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.Talent;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TalentRepository extends PagingAndSortingRepository<Talent, UUID>,
    QueryDslPredicateExecutor<Talent> {

  Talent findByLinkedInIdOrEmail(String linkedInId, String email);

  Talent findByUsername(String username);
}
