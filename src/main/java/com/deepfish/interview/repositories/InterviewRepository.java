package com.deepfish.interview.repositories;

import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.domain.InterviewStatus;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
public interface InterviewRepository extends PagingAndSortingRepository<Interview, UUID>,
    QueryDslPredicateExecutor<Interview> {

  List<Interview> findBySharedId(UUID sharedId);

  List<Interview> findByEmployerIdAndStartDateTimeAfterAndStatusIn(
      UUID employerId,
      ZonedDateTime startDateTimeAfter,
      List<InterviewStatus> statuses
  );

  List<Interview> findByTalentIdAndStartDateTimeAfterAndStatusIn(
      UUID talentId,
      ZonedDateTime startDateTimeAfter,
      List<InterviewStatus> statuses
  );

  @PreAuthorize("hasRole('ADMIN')")
  Page<Interview> findByTalentLastNameContainingOrTalentFirstNameContainingAllIgnoreCase(
      @Param("talentLastName") String talentLastName,
      @Param("talentFirstName") String talentFirstName,
      Pageable pageable
  );

  @PreAuthorize("hasAnyRole('ADMIN', 'TALENT')")
  @PostAuthorize("hasRole('ADMIN') or returnObject.?[talent.linkedinId != #root.principal].size() == 0")
  @Query("select itw from #{#entityName} itw "
      + "join itw.opportunity opp on opp.id = :opportunityId "
      + "where itw.startDateTime > :startAfter "
      + "and itw.status = '#{T(com.deepfish.interview.domain.InterviewStatus).TENTATIVE}' "
      + "and itw.talentResponseStatus = '#{T(com.deepfish.interview.domain.ParticipationStatus).NEEDS_ACTION}' "
      + "or itw.status = '#{T(com.deepfish.interview.domain.InterviewStatus).CONFIRMED}' "
      + "order by itw.startDateTime desc")
  List<Interview> findByOpportunityIdAndCurrentlyTalentPendingOrConfirmed(
      @Param("opportunityId") UUID opportunityId,
      @Param("startAfter") @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime startAfter
  );
}
