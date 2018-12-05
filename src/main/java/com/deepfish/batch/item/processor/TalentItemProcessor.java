package com.deepfish.batch.item.processor;

import com.deepfish.talent.domain.Talent;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.batch.item.ItemProcessor;

public class TalentItemProcessor implements ItemProcessor<Talent, Talent> {

  @Override
  public Talent process(Talent talent) throws Exception {
    talent.setProfileCompletenessLastCalculatedAt(LocalDateTime.now(Clock.systemUTC()));
    return talent;
  }
}
