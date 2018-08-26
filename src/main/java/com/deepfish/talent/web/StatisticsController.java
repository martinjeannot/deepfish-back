package com.deepfish.talent.web;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class StatisticsController {

  private final EntityManager entityManager;

  public StatisticsController(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @GetMapping("opportunities/statistics")
  @ResponseBody
  public ResponseEntity getOpportunityStatistics(@RequestParam("datepart") String datepart) {
    Query query = entityManager.createNativeQuery(
        "SELECT date_part(:datepart, created_at) AS datepart, count(1) FROM Opportunity GROUP BY datepart ORDER BY datepart");
    query.setParameter("datepart", datepart);
    return ResponseEntity.ok(query.getResultList());
  }
}
