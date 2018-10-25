package com.deepfish.talent.web;

import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import java.util.List;
import java.util.Objects;
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
      @RequestParam("group-by") String groupBy,
      @RequestParam(name = "qualification-ranking", required = false) List<String> qualificationRankings
  ) {
    String[] datePatternAndInterval = extractDatePatternAndInterval(groupBy);
    String datePattern = datePatternAndInterval[0];
    String interval = datePatternAndInterval[1];

    String talentSqlString = "SELECT to_char(created_at, :date_pattern) AS dtime, count(1) AS talents FROM Talent ";
    if (Objects.nonNull(qualificationRankings) && !qualificationRankings.isEmpty()) {
      talentSqlString +=
          "JOIN Qualification ON Qualification.talent_id = Talent.id WHERE Qualification.ranking in ("
              + String.join(", ", qualificationRankings) + ") ";
    }
    talentSqlString += "GROUP BY dtime";
    Query query = entityManager.createNativeQuery(
        "SELECT dtime, coalesce(talents, 0) AS talents "
            + "FROM (SELECT to_char(time_series, :date_pattern) AS dtime FROM generate_series(cast(:start_date AS DATE), cast(:end_date AS DATE), cast(:time_interval AS INTERVAL)) time_series) all_times "
            + "LEFT JOIN (" + talentSqlString + ") talents "
            + "USING (dtime) ORDER BY dtime");
    query.setParameter("date_pattern", datePattern);
    query.setParameter("start_date", createdAtAfter);
    query.setParameter("end_date", createdAtBefore);
    query.setParameter("time_interval", interval);
    return ResponseEntity.ok(query.getResultList());
  }

  @GetMapping("opportunities/statistics")
  @ResponseBody
  public ResponseEntity getOpportunityStatistics(
      @RequestParam("created-at-after") String createdAtAfter,
      @RequestParam("created-at-before") String createdAtBefore,
      @RequestParam("group-by") String groupBy,
      @RequestParam(name = "talent-status", required = false) OpportunityStatus talentStatus,
      @RequestParam(name = "employer-status", required = false) OpportunityStatus employerStatus
  ) {
    String[] datePatternAndInterval = extractDatePatternAndInterval(groupBy);
    String datePattern = datePatternAndInterval[0];
    String interval = datePatternAndInterval[1];

    String opportunitySqlString = "SELECT to_char(created_at, :date_pattern) AS dtime, count(1) AS opportunities FROM Opportunity ";
    if (Objects.nonNull(talentStatus)) {
      opportunitySqlString += opportunitySqlString.contains(" WHERE ") ? "" : "WHERE ";
      opportunitySqlString += "talent_status = :talent_status ";
    }
    if (Objects.nonNull(employerStatus)) {
      opportunitySqlString += opportunitySqlString.contains(" WHERE ") ? "AND " : "WHERE ";
      opportunitySqlString += "employer_status = :employer_status ";
    }
    opportunitySqlString += "GROUP BY dtime";
    Query query = entityManager.createNativeQuery(
        "SELECT dtime, coalesce(opportunities, 0) AS opportunities "
            + "FROM (SELECT to_char(time_series, :date_pattern) AS dtime FROM generate_series(cast(:start_date AS DATE), cast(:end_date AS DATE), cast(:time_interval AS INTERVAL)) time_series) all_times "
            + "LEFT JOIN (" + opportunitySqlString + ") opportunities "
            + "USING (dtime) ORDER BY dtime");
    query.setParameter("date_pattern", datePattern);
    query.setParameter("start_date", createdAtAfter);
    query.setParameter("end_date", createdAtBefore);
    query.setParameter("time_interval", interval);
    if (Objects.nonNull(talentStatus)) {
      query.setParameter("talent_status", talentStatus.toString());
    }
    if (Objects.nonNull(employerStatus)) {
      query.setParameter("employer_status", employerStatus.toString());
    }
    return ResponseEntity.ok(query.getResultList());
  }

  private String[] extractDatePatternAndInterval(String groupBy) {
    switch (groupBy) {
      case "day":
        return new String[]{"YYYY-MM-DD", "1 day"};
      case "week":
        return new String[]{"YYYY-WW", "1 week"};
      case "month":
        return new String[]{"YYYY-MM", "1 month"};
      case "year":
        return new String[]{"YYYY", "1 year"};
      default:
        throw new IllegalArgumentException("Unknown group-by value : " + groupBy);
    }
  }
}
