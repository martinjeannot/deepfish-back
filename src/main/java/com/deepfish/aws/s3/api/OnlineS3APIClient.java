package com.deepfish.aws.s3.api;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class OnlineS3APIClient implements S3APIClient {

  private final S3Client s3Client;

  private final String bucketName;

  public OnlineS3APIClient(S3Client s3Client, String bucketName) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }


  @Override
  public void upload(MultipartFile file, String targetURI) {
    try {
      s3Client.putObject(
          PutObjectRequest
              .builder()
              .bucket(bucketName)
              .acl(ObjectCannedACL.PUBLIC_READ)
              .key(targetURI)
              .build(),
          RequestBody.fromBytes(file.getBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(String targetURI) {
    s3Client.deleteObject(
        DeleteObjectRequest
            .builder()
            .bucket(bucketName)
            .key(targetURI)
            .build());
  }
}
