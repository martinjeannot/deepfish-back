package com.deepfish.aws.s3.api;

import org.springframework.web.multipart.MultipartFile;

public class LocalS3APIClient implements S3APIClient {

  @Override
  public void upload(MultipartFile file, String targetURI) {
    throw new UnsupportedOperationException(); // TODO
  }

  @Override
  public void delete(String targetURI) {
    throw new UnsupportedOperationException(); // TODO
  }
}
