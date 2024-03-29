package com.deepfish.talent.web;

import com.deepfish.core.web.AbstractStatisticsController;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.Opportunity;
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
public class TalentStatisticsController extends AbstractStatisticsController {

  public TalentStatisticsController(
      EntityManager entityManager
  ) {
    super(entityManager);
  }

  @GetMapping("talents/statistics")
  @ResponseBody
  public ResponseEntity getTalentAcquisitionStatistics(
      @RequestParam("start-date") String startDate,
      @RequestParam("end-date") String endDate,
      @RequestParam("group-by") String groupBy,
      @RequestParam(name = "event-field", defaultValue = "createdAt") String eventField,
      @RequestParam(name = "qualification-ranking", required = false) List<String> qualificationRankings
  ) {
    String[] datePatternAndInterval = extractDatePatternAndInterval(groupBy);
    String datePattern = datePatternAndInterval[0];
    String interval = datePatternAndInterval[1];

    String talentSqlString = "SELECT to_char("
        + checkEventFieldName(Talent.class, eventField)
        + ", :date_pattern) AS dtime, count(1) AS talents FROM Talent ";
    if (Objects.nonNull(qualificationRankings) && !qualificationRankings.isEmpty()) {
      talentSqlString +=
          "JOIN Qualification ON Qualification.talent_id = Talent.id WHERE Qualification.ranking in ("
              + String.join(", ", qualificationRankings) + ") ";
    }
    talentSqlString += "GROUP BY dtime";
    Query query = getEntityManager().createNativeQuery(
        "SELECT dtime, coalesce(talents, 0) AS talents "
            + "FROM (SELECT to_char(time_series, :date_pattern) AS dtime FROM generate_series(cast(:start_date AS DATE), cast(:end_date AS DATE), cast(:time_interval AS INTERVAL)) time_series) all_times "
            + "LEFT JOIN (" + talentSqlString + ") talents "
            + "USING (dtime) ORDER BY dtime");
    query.setParameter("date_pattern", datePattern);
    query.setParameter("start_date", startDate);
    query.setParameter("end_date", endDate);
    query.setParameter("time_interval", interval);
    return ResponseEntity.ok(query.getResultList());
  }

  @GetMapping("opportunities/statistics")
  @ResponseBody
  public ResponseEntity getOpportunityStatistics(
      @RequestParam("start-date") String startDate,
      @RequestParam("end-date") String endDate,
      @RequestParam("group-by") String groupBy,
      @RequestParam(name = "event-field", defaultValue = "createdAt") String eventField,
      @RequestParam(name = "talent-status", required = false) OpportunityStatus talentStatus,
      @RequestParam(name = "employer-status", required = false) OpportunityStatus employerStatus
  ) {
    String[] datePatternAndInterval = extractDatePatternAndInterval(groupBy);
    String datePattern = datePatternAndInterval[0];
    String interval = datePatternAndInterval[1];

    String opportunitySqlString = "SELECT to_char("
        + checkEventFieldName(Opportunity.class, eventField)
        + ", :date_pattern) AS dtime, count(1) AS opportunities FROM Opportunity ";
    if (Objects.nonNull(talentStatus)) {
      opportunitySqlString += opportunitySqlString.contains(" WHERE ") ? "" : "WHERE ";
      opportunitySqlString += "talent_status = :talent_status ";
    }
    if (Objects.nonNull(employerStatus)) {
      opportunitySqlString += opportunitySqlString.contains(" WHERE ") ? "AND " : "WHERE ";
      opportunitySqlString += "employer_status = :employer_status ";
    }
    opportunitySqlString += "GROUP BY dtime";
    Query query = getEntityManager().createNativeQuery(
        "SELECT dtime, coalesce(opportunities, 0) AS opportunities "
            + "FROM (SELECT to_char(time_series, :date_pattern) AS dtime FROM generate_series(cast(:start_date AS DATE), cast(:end_date AS DATE), cast(:time_interval AS INTERVAL)) time_series) all_times "
            + "LEFT JOIN (" + opportunitySqlString + ") opportunities "
            + "USING (dtime) ORDER BY dtime");
    query.setParameter("date_pattern", datePattern);
    query.setParameter("start_date", startDate);
    query.setParameter("end_date", endDate);
    query.setParameter("time_interval", interval);
    if (Objects.nonNull(talentStatus)) {
      query.setParameter("talent_status", talentStatus.toString());
    }
    if (Objects.nonNull(employerStatus)) {
      query.setParameter("employer_status", employerStatus.toString());
    }
    return ResponseEntity.ok(query.getResultList());
  }
}
