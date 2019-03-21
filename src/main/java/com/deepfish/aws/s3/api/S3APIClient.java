package com.deepfish.aws.s3.api;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service used to aws files
 */
public interface S3APIClient {

  /**
   * Upload the given file to the given relative URI
   *
   * @param file the file to aws
   * @param targetURI the relative URI to aws the image to
   */
  void upload(MultipartFile file, String targetURI);

  /**
   * Delete the resource(s) targeted by the given relative URI
   *
   * @param targetURI the relative URI
   */
  void delete(String targetURI);
}
