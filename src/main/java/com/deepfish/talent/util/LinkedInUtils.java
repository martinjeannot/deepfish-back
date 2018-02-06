package com.deepfish.talent.util;

public final class LinkedInUtils {

  public static final String EMAIL_PROFILE_URI = "/v1/people/~:("
      + LinkedInUtils.EMAIL_FIELDS + ","
      + LinkedInUtils.BASIC_PROFILE_FIELDS
      + ")";

  public static final String EMAIL_FIELDS = "email-address";

  public static final String BASIC_PROFILE_FIELDS = ""
      + "id,"
      + "first-name,"
      + "last-name,"
      //+ "maiden-name,"
      + "formatted-name,"
      //+ "phonetic-first-name,"
      //+ "phonetic-last-name,"
      //+ "formatted-phonetic-name,"
      + "headline,"
      + "location,"
      + "industry,"
      //+ "current-share,"
      + "num-connections,"
      + "num-connections-capped,"
      + "summary,"
      + "specialties,"
      + "positions,"
      + "picture-url,"
      + "picture-urls::(original),"
      + "site-standard-profile-request,"
      + "api-standard-profile-request,"
      + "public-profile-url";
}
