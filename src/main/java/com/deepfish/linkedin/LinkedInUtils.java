package com.deepfish.linkedin;

public final class LinkedInUtils {

  public static final String LITE_PROFILE_URI = "/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";

  public static final String EMAIL_ADDRESS_URI = "/v2/emailAddress?q=members&projection=(elements*(handle~))";
}
