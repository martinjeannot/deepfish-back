package com.deepfish.aws.s3.api;

import java.nio.ByteBuffer;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class OnlineS3APIClient implements S3APIClient {

  private final S3Client s3Client;

  private final String bucketName;

  public OnlineS3APIClient(
      S3Client s3Client,
      String bucketName
  ) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }

  @Override
  public void put(String targetURI, byte[] payload) {
    put(targetURI, (Object) payload);
  }

  @Override
  public void put(String targetURI, ByteBuffer payload) {
    put(targetURI, (Object) payload);
  }

  private void put(String targetURI, Object payload) {
    RequestBody requestBody;
    if (payload instanceof byte[]) {
      requestBody = RequestBody.fromBytes((byte[]) payload);
    } else if (payload instanceof ByteBuffer) {
      requestBody = RequestBody.fromByteBuffer((ByteBuffer) payload);
    } else {
      throw new IllegalArgumentException("Unknown payload type : " + payload.getClass().toString());
    }

    s3Client.putObject(
        PutObjectRequest
            .builder()
            .bucket(bucketName)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .key(targetURI)
            .build(),
        requestBody);
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
