package com.deepfish.aws.s3.api;

import java.nio.ByteBuffer;

public interface S3APIClient {

  /**
   * Send the given payload to the given relative URI
   *
   * @param targetURI the relative URI to send the payload to
   * @param payload the payload to send
   */
  void put(String targetURI, byte[] payload);

  /**
   * Send the given payload to the given relative URI
   *
   * @param targetURI the relative URI to send the payload to
   * @param payload the payload to send
   */
  void put(String targetURI, ByteBuffer payload);

  /**
   * Delete the resource(s) targeted by the given relative URI
   *
   * @param targetURI the relative URI
   */
  void delete(String targetURI);
}
