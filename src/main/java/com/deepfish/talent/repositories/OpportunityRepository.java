package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OpportunityRepository extends PagingAndSortingRepository<Opportunity, UUID>,
    QueryDslPredicateExecutor<Opportunity> {

  Page<Opportunity> findByRequirementCompanyNameContainingOrTalentLastNameContainingOrTalentFirstNameContainingAllIgnoreCase(
      @Param("companyName") String companyName,
      @Param("talentLastName") String talentLastName,
      @Param("talentFirstName") String talentFirstName,
      Pageable pageable);

  Page<Opportunity> findByRequirementId(
      @Param("requirementId") UUID requirementId,
      Pageable pageable);

  Page<Opportunity> findByTalentStatusAndCreatedAtBetween(
      OpportunityStatus talentStatus,
      LocalDateTime createdAtAfter,
      LocalDateTime createdAtBefore,
      Pageable pageable);
}
