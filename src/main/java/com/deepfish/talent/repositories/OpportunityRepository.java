package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

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

  @PreAuthorize("hasRole('ADMIN')")
  @Query("select opp from #{#entityName} opp "
      + "join opp.requirement req "
      + "join req.company comp "
      + "where opp.talent.id = :talentId "
      + "and opp.employerStatus in ('#{T(com.deepfish.talent.domain.opportunity.OpportunityStatus).PENDING}', '#{T(com.deepfish.talent.domain.opportunity.OpportunityStatus).ACCEPTED}') "
      + "and upper(comp.name) like upper(concat('%', :companyName, '%'))")
  Page<Opportunity> findInterviewSchedulableByTalentIdAndCompanyNameContainingAllIgnoreCase(
      @Param("talentId") UUID talentId,
      @Param("companyName") String companyName,
      Pageable pageable
  );

  Page<Opportunity> findByTalentStatusAndCreatedAtBetween(
      OpportunityStatus talentStatus,
      LocalDateTime createdAtAfter,
      LocalDateTime createdAtBefore,
      Pageable pageable);

  long countByRequirementIdAndTalentStatus(UUID requirementId, OpportunityStatus talentStatus);
}
