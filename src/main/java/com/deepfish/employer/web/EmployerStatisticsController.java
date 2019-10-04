package com.deepfish.employer.web;

import com.deepfish.core.web.AbstractStatisticsController;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestController
public class EmployerStatisticsController extends AbstractStatisticsController {

  public EmployerStatisticsController(
      EntityManager entityManager
  ) {
    super(entityManager);
  }

  @GetMapping("requirements/statistics")
  public ResponseEntity getRequirementsStatistics(
      @RequestParam("created-at-after") String createdAtAfter,
      @RequestParam("created-at-before") String createdAtBefore,
      @RequestParam("group-by") String groupBy
  ) {
    String[] datePatternAndInterval = extractDatePatternAndInterval(groupBy);
    String datePattern = datePatternAndInterval[0];
    String interval = datePatternAndInterval[1];

    String requirementSqlString = "SELECT to_char(created_at, :date_pattern) AS dtime, count(1) AS requirements FROM Requirement ";
    requirementSqlString += "GROUP BY dtime";
    Query query = getEntityManager().createNativeQuery(
        "SELECT dtime, coalesce(requirements, 0) AS requirements "
            + "FROM (SELECT to_char(time_series, :date_pattern) AS dtime FROM generate_series(cast(:start_date AS DATE), cast(:end_date AS DATE), cast(:time_interval AS INTERVAL)) time_series) all_times "
            + "LEFT JOIN (" + requirementSqlString + ") requirements "
            + "USING (dtime) ORDER BY dtime");
    query.setParameter("date_pattern", datePattern);
    query.setParameter("start_date", createdAtAfter);
    query.setParameter("end_date", createdAtBefore);
    query.setParameter("time_interval", interval);
    return ResponseEntity.ok(query.getResultList());
  }
}
