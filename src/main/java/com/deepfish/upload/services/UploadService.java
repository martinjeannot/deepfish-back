package com.deepfish.upload.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service used to upload files
 */
public interface UploadService {

  /**
   * Upload the given file to the given relative URI
   *
   * @param file the file to upload
   * @param targetURI the relative URI to upload the image to
   */
  void upload(MultipartFile file, String targetURI);

  /**
   * Delete the resource(s) targeted by the given relative URI
   *
   * @param targetURI the relative URI
   */
  void delete(String targetURI);
}
