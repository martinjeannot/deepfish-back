package com.deepfish.batch.item.reader;

import com.deepfish.talent.domain.QTalent;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.data.domain.Sort.Direction;

/**
 * This class mainly contains static factories. To be replaced after Spring Batch 4+ migration by
 * RepositoryItemReaderBuilders
 */
public class TalentItemReader {

  private TalentItemReader() {
    throw new AssertionError();
  }

  public static ItemReader<Talent> newInstance(
      TalentRepository talentRepository
  ) {
    RepositoryItemReader<Talent> itemReader = new RepositoryItemReader<>();
    itemReader.setRepository(talentRepository);
    itemReader.setMethodName("findAll");
    itemReader.setPageSize(100);
    Map<String, Direction> sorts = new HashMap<>();
    sorts.put("createdAt", Direction.DESC);
    itemReader.setSort(sorts);
    return itemReader;
  }

  public static ItemReader<Talent> newInstance(
      TalentRepository talentRepository,
      LocalDateTime createdAtAfter,
      LocalDateTime createdAtBefore,
      boolean active
  ) {
    RepositoryItemReader<Talent> itemReader = new RepositoryItemReader<>();
    itemReader.setRepository(talentRepository);
    itemReader.setMethodName("findAll");
    itemReader.setArguments(Collections.singletonList(
        QTalent.talent.createdAt.between(createdAtAfter, createdAtBefore)
            .and(QTalent.talent.active.eq(active))));
    itemReader.setPageSize(100);
    Map<String, Direction> sorts = new HashMap<>();
    sorts.put("createdAt", Direction.DESC);
    itemReader.setSort(sorts);
    return itemReader;
  }

  public static ItemReader<Talent> newInstance(
      TalentRepository talentRepository,
      LocalDate reactivatedOnBefore,
      boolean active
  ) {
    RepositoryItemReader<Talent> itemReader = new RepositoryItemReader<>();
    itemReader.setRepository(talentRepository);
    itemReader.setMethodName("findAll");
    itemReader.setArguments(Collections.singletonList(
        QTalent.talent.reactivatedOn.before(reactivatedOnBefore)
            .and(QTalent.talent.active.eq(active))));
    itemReader.setPageSize(100);
    Map<String, Direction> sorts = new HashMap<>();
    sorts.put("createdAt", Direction.DESC);
    itemReader.setSort(sorts);
    return itemReader;
  }

  public static ItemReader<UUID> newInstance(
      DataSource dataSource,
      LocalDateTime talentRespondedAtAfter,
      LocalDateTime talentRespondedAtBefore
  ) {
    JdbcCursorItemReader<UUID> itemReader = new JdbcCursorItemReader<>();
    itemReader.setDataSource(dataSource);
    itemReader.setSql(
        "SELECT tal.id "
            + "FROM talent tal "
            + "JOIN opportunity opp3 ON opp3.talent_id = tal.id "
            + "JOIN ("
            + "SELECT opp1.talent_id, MIN(opp1.talent_responded_at) AS first_acceptance_at "
            + "FROM opportunity opp1 "
            + "WHERE opp1.talent_status = 'ACCEPTED' "
            + "GROUP BY opp1.talent_id"
            + ") opp2 ON opp2.talent_id = opp3.talent_id "
            + "AND opp2.first_acceptance_at = opp3.talent_responded_at "
            + "JOIN qualification qlf ON qlf.talent_id = tal.id "
            + "WHERE opp3.talent_status = 'ACCEPTED' "
            + "AND opp3.talent_responded_at >= '" + talentRespondedAtAfter.toString() + "' "
            + "AND opp3.talent_responded_at < '" + talentRespondedAtBefore.toString() + "' "
            + "AND qlf.interview_scheduled = FALSE");
    itemReader.setRowMapper((rs, rowNum) -> rs.getObject(1, UUID.class));
    return itemReader;
  }
}
