package com.deepfish.batch.item.writer;

import com.deepfish.talent.domain.opportunity.Opportunity;
import com.deepfish.talent.repositories.OpportunityRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;

public class OpportunityItemWriter {

  private OpportunityItemWriter() {
    throw new AssertionError();
  }

  /**
   * Static factory. To be replaced after Spring Batch 4+ migration by a
   * RepositoryItemWriterBuilder
   */
  public static ItemWriter<Opportunity> newInstance(OpportunityRepository opportunityRepository) {
    RepositoryItemWriter<Opportunity> itemWriter = new RepositoryItemWriter<>();
    itemWriter.setRepository(opportunityRepository);
    itemWriter.setMethodName("save");
    return itemWriter;
  }
}
