package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.Talent;
import java.util.List;
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

  @PreAuthorize("hasRole('ADMIN') or hasRole('TRUSTED_CLIENT')")
  Talent findByUsername(String username);

  Talent findByEmail(String email);

  Talent findByFirstNameAndLastName(String firstName, String lastName);

  Page<Talent> findByEmailContainingOrLastNameContainingOrFirstNameContainingOrPhoneNumberContainingAllIgnoreCase(
      @Param("email") String email,
      @Param("lastName") String lastName,
      @Param("firstName") String firstName,
      @Param("phoneNumber") String phoneNumber,
      Pageable pageable);

  List<Talent> findFirst10ByLinkedinProfileLastRetrievalAttemptedAtIsNullAndLinkedinPublicProfileUrlIsNotNullOrderByCreatedAtDesc();

  @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER') or hasRole('TALENT')")
  @PostAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER') or returnObject.linkedinId == principal")
  @Override
  Talent findOne(UUID uuid);

  @PreAuthorize("hasRole('ADMIN') or #entity.linkedinId == principal")
  @Override
  <S extends Talent> S save(S entity);
}
