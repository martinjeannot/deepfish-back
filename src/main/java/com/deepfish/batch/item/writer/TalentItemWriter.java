package com.deepfish.batch.item.writer;

import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;

public class TalentItemWriter {

  private TalentItemWriter() {
    throw new AssertionError();
  }

  /**
   * Static factory. To be replaced after Spring Batch 4+ migration by a
   * RepositoryItemWriterBuilder
   */
  public static ItemWriter<Talent> newInstance(TalentRepository talentRepository) {
    RepositoryItemWriter<Talent> itemWriter = new RepositoryItemWriter<>();
    itemWriter.setRepository(talentRepository);
    itemWriter.setMethodName("save");
    return itemWriter;
  }
}
