package com.deepfish.batch.item.reader;

import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.domain.opportunity.QOpportunity;
import com.deepfish.talent.repositories.OpportunityRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort.Direction;

public class OpportunityItemReader {

  private OpportunityItemReader() {
    throw new AssertionError();
  }

  /**
   * Static factory. To be replaced after Spring Batch 4+ migration by a
   * RepositoryItemReaderBuilder
   */
  public static ItemReader<Opportunity> newInstance(
      OpportunityRepository opportunityRepository,
      LocalDateTime createdAtBefore,
      OpportunityStatus talentStatus
  ) {
    RepositoryItemReader<Opportunity> itemReader = new RepositoryItemReader<>();
    itemReader.setRepository(opportunityRepository);
    itemReader.setMethodName("findAll");
    itemReader.setArguments(Collections.singletonList(
        QOpportunity.opportunity.createdAt.before(createdAtBefore)
            .and(QOpportunity.opportunity.talentStatus.eq(talentStatus))));
    itemReader.setPageSize(100);
    Map<String, Direction> sorts = new HashMap<>();
    sorts.put("createdAt", Direction.DESC);
    itemReader.setSort(sorts);
    return itemReader;
  }
}
