package com.deepfish.aws.s3.api;

import java.nio.ByteBuffer;

public class LocalS3APIClient implements S3APIClient {

  @Override
  public void put(String targetURI, byte[] payload) {
    throw new UnsupportedOperationException(); // TODO
  }

  @Override
  public void put(String targetURI, ByteBuffer payload) {
    throw new UnsupportedOperationException(); // TODO
  }

  @Override
  public void delete(String targetURI) {
    throw new UnsupportedOperationException(); // TODO
  }
}
