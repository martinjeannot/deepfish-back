package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.opportunity.OpportunityDatum;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OpportunityDatumRepository extends
    PagingAndSortingRepository<OpportunityDatum, LocalDateTime> {

  @Query("SELECT new com.deepfish.talent.domain.opportunity.OpportunityDatum("
      + "COUNT(1) AS total_created, "
      + "SUM(CASE WHEN talent_status = 'PENDING' THEN 1 ELSE 0 END) AS total_talent_pending, "
      + "SUM(CASE WHEN employer_status = 'PENDING' THEN 1 ELSE 0 END) AS total_employer_pending, "
      + "SUM(CASE WHEN talent_status = 'ACCEPTED' THEN 1 ELSE 0 END) AS total_talent_accepted, "
      + "SUM(CASE WHEN employer_status = 'ACCEPTED' THEN 1 ELSE 0 END) AS total_employer_accepted, "
      + "SUM(CASE WHEN talent_status = 'DECLINED' THEN 1 ELSE 0 END) AS total_talent_declined, "
      + "SUM(CASE WHEN employer_status = 'DECLINED' THEN 1 ELSE 0 END) AS total_employer_declined, "
      + "SUM(CASE WHEN talent_status = 'EXPIRED' THEN 1 ELSE 0 END) AS total_talent_expired"
      + ") FROM Opportunity")
  OpportunityDatum findCurrent();

  OpportunityDatum findFirstByOrderByCreatedAtDesc();
}
