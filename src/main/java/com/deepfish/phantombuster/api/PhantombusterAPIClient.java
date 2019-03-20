package com.deepfish.phantombuster.api;

import com.deepfish.talent.domain.Talent;
import java.util.List;
import java.util.Map;

public interface PhantombusterAPIClient {

  List<Map<String, Object>> launchLinkedinProfileScraperAgent(
      int id,
      String output,
      List<Talent> talents
  );

  Map<String, Object> launchAgent(
      int id,
      String output,
      Map<String, Object> argument
  );
}
