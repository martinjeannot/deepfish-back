package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.Talent;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
@PreAuthorize("hasRole('ADMIN')")
public interface TalentRepository extends PagingAndSortingRepository<Talent, UUID>,
    QueryDslPredicateExecutor<Talent> {

  Talent findByLinkedInIdOrEmail(String linkedInId, String email);

  @PreAuthorize("hasRole('TRUSTED_CLIENT')")
  Talent findByUsername(String username);

  Page<Talent> findByEmailContainingOrLastNameContainingOrFirstNameContainingAllIgnoreCase(
      @Param("email") String email,
      @Param("lastName") String lastName,
      @Param("firstName") String firstName,
      Pageable pageable);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TALENT')")
  @PostAuthorize("returnObject.linkedInId == principal")
  @Override
  Talent findOne(UUID uuid);

  @PreAuthorize("hasRole('ADMIN') or #entity.linkedInId == principal")
  @Override
  <S extends Talent> S save(S entity);
}
