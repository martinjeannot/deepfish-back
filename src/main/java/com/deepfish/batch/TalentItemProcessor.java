package com.deepfish.batch;

import com.deepfish.talent.domain.Talent;
import org.springframework.batch.item.ItemProcessor;

public class TalentItemProcessor implements ItemProcessor<Talent, Talent> {

  @Override
  public Talent process(Talent item) throws Exception {
    System.out.println(item.getLastName());
    return item;
  }
}
