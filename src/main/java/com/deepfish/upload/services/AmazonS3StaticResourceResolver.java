package com.deepfish.upload.services;

import java.net.URI;

public class AmazonS3StaticResourceResolver implements StaticResourceResolver {

  private final URI staticResourceDomain;

  public AmazonS3StaticResourceResolver(String bucketName) {
    this.staticResourceDomain = URI.create("https://" + bucketName + ".s3.amazonaws.com");
  }

  @Override
  public URI resolve(String targetURI) {
    return staticResourceDomain.resolve(targetURI);
  }
}
