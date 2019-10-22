package com.deepfish.interview.web;

import com.deepfish.core.web.AbstractStatisticsController;
import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.domain.InterviewStatus;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestController
public class InterviewStatisticsController extends AbstractStatisticsController {

  public InterviewStatisticsController(
      EntityManager entityManager
  ) {
    super(entityManager);
  }

  @GetMapping("interviews/statistics")
  public ResponseEntity getInterviewsStatistics(
      @RequestParam("start-date") String startDate,
      @RequestParam("end-date") String endDate,
      @RequestParam("group-by") String groupBy,
      @RequestParam(name = "event-field", defaultValue = "startDateTime") String eventField,
      @RequestParam(name = "status", required = false) InterviewStatus status
  ) {
    String[] datePatternAndInterval = extractDatePatternAndInterval(groupBy);
    String datePattern = datePatternAndInterval[0];
    String interval = datePatternAndInterval[1];

    String interviewSqlString = "SELECT to_char("
        + checkEventFieldName(Interview.class, eventField)
        + ", :date_pattern) AS dtime, count(1) AS interviews FROM Interview ";
    if (Objects.nonNull(status)) {
      interviewSqlString += interviewSqlString.contains(" WHERE ") ? "" : "WHERE ";
      interviewSqlString += "status = :status ";
    }
    interviewSqlString += "GROUP BY dtime";
    Query query = getEntityManager().createNativeQuery(
        "SELECT dtime, coalesce(interviews, 0) AS interviews "
            + "FROM (SELECT to_char(time_series, :date_pattern) AS dtime FROM generate_series(cast(:start_date AS DATE), cast(:end_date AS DATE), cast(:time_interval AS INTERVAL)) time_series) all_times "
            + "LEFT JOIN (" + interviewSqlString + ") interviews "
            + "USING (dtime) ORDER BY dtime");
    query.setParameter("date_pattern", datePattern);
    query.setParameter("start_date", startDate);
    query.setParameter("end_date", endDate);
    query.setParameter("time_interval", interval);
    if (Objects.nonNull(status)) {
      query.setParameter("status", status.toString());
    }
    return ResponseEntity.ok(query.getResultList());
  }
}
