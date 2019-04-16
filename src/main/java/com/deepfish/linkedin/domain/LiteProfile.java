package com.deepfish.linkedin.domain;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * LinkedIn lite profile
 *
 * @see <a href="https://docs.microsoft.com/en-us/linkedin/shared/references/v2/profile/lite-profile">lite
 * profile fields</a>
 */
@Data
public class LiteProfile {

  private final Map<String, Object> liteProfile;

  public LiteProfile(Map<String, Object> liteProfile) {
    this.liteProfile = liteProfile;
  }

  public String getId() {
    return (String) liteProfile.get("id");
  }

  public String getFirstName() {
    return (String) ((Map) ((Map) liteProfile
        .get("firstName"))
        .get("localized"))
        .values()
        .iterator()
        .next();
  }

  public String getLastName() {
    return (String) ((Map) ((Map) liteProfile
        .get("lastName"))
        .get("localized"))
        .values()
        .iterator()
        .next();
  }

  public String getProfilePictureUrl() {
    if (liteProfile.containsKey("profilePicture")) {
      List identifiers = (List) ((Map) ((Map) liteProfile
          .get("profilePicture"))
          .get("displayImage~"))
          .get("elements");
      return (String) ((Map) ((List) ((Map) identifiers
          .get(identifiers.size() - 1))
          .get("identifiers"))
          .get(0))
          .get("identifier");
    }
    return "https://app.deepfish.co/static/img/avatar.png";
  }
}
