package com.deepfish.batch.item.reader;

import com.deepfish.talent.domain.QTalent;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
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
}
