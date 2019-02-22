package com.deepfish.batch.item.processor;

import com.deepfish.talent.domain.Talent;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.batch.item.ItemProcessor;

public class TalentMigrator implements ItemProcessor<Talent, Talent> {

  @Override
  public Talent process(Talent talent) throws Exception {
    if (Objects.nonNull(talent.getBasicProfile())) {
      // linkedin public profile url
      if (Objects.nonNull(talent.getBasicProfile().get("publicProfileUrl"))) {
        talent.setLinkedinPublicProfileUrl(
            talent.getBasicProfile().get("publicProfileUrl").toString());
      } else if (Objects.nonNull(talent.getBasicProfile().get("siteStandardProfileRequest"))) {
        talent.setLinkedinPublicProfileUrl(
            ((Map) talent.getBasicProfile().get("siteStandardProfileRequest"))
                .get("url")
                .toString());
      }

      // profile picture url
      if (Objects.nonNull(talent.getBasicProfile().get("pictureUrl"))) {
        talent.setProfilePictureUrl(
            talent.getBasicProfile().get("pictureUrl").toString());
      } else if (Objects.nonNull(talent.getBasicProfile().get("pictureUrls"))) {
        List values = (List) ((Map) talent.getBasicProfile().get("pictureUrls")).get("values");
        if (!values.isEmpty()) {
          talent.setProfilePictureUrl(
              ((List) ((Map) talent.getBasicProfile()
                  .get("pictureUrls"))
                  .get("values"))
                  .get(0)
                  .toString()
          );
        }
      }
    }
    return talent;
  }
}
