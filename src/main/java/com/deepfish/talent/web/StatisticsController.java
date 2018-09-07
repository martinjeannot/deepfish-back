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

  @GetMapping("talents/statistics")
  @ResponseBody
  public ResponseEntity getTalentAcquisitionStatistics(
      @RequestParam("created-at-after") String createdAtAfter,
      @RequestParam("created-at-before") String createdAtBefore,
      @RequestParam("group-by") String groupBy) {
    String datePattern;
    String interval;
    switch (groupBy) {
      case "day":
        datePattern = "YYYY-MM-DD";
        interval = "1 day";
        break;
      case "week":
        datePattern = "YYYY-WW";
        interval = "1 week";
        break;
      case "month":
        datePattern = "YYYY-MM";
        interval = "1 month";
        break;
      case "year":
        datePattern = "YYYY";
        interval = "1 year";
        break;
      default:
        throw new IllegalArgumentException("Unknown group-by value : " + groupBy);
    }
    Query query = entityManager.createNativeQuery(
        "SELECT dtime, coalesce(talents, 0) AS talents "
            + "FROM (SELECT to_char(time_series, :date_pattern) AS dtime FROM generate_series(cast(:start_date AS DATE), cast(:end_date AS DATE), cast(:time_interval AS INTERVAL)) time_series) all_times "
            + "LEFT JOIN (SELECT to_char(created_at, :date_pattern) AS dtime, count(1) AS talents FROM Talent GROUP BY dtime) talents "
            + "USING (dtime) ORDER BY dtime");
    query.setParameter("date_pattern", datePattern);
    query.setParameter("start_date", createdAtAfter);
    query.setParameter("end_date", createdAtBefore);
    query.setParameter("time_interval", interval);
    return ResponseEntity.ok(query.getResultList());
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
