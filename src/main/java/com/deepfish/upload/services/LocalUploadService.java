package com.deepfish.upload.services;

import org.springframework.web.multipart.MultipartFile;

public class LocalUploadService implements UploadService {

  @Override
  public void upload(MultipartFile file, String targetURI) {
    throw new UnsupportedOperationException(); // TODO
  }

  @Override
  public void delete(String targetURI) {
    throw new UnsupportedOperationException(); // TODO
  }
}
