package com.deepfish.aws.s3;

import java.net.URI;

public class OnlineStaticResourceResolver implements StaticResourceResolver {

  private final URI staticResourceDomain;

  public OnlineStaticResourceResolver(String bucketName) {
    this.staticResourceDomain = URI.create("https://" + bucketName + ".s3.amazonaws.com");
  }

  @Override
  public URI resolve(String targetURI) {
    return staticResourceDomain.resolve(targetURI);
  }
}
