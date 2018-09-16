package com.deepfish.upload.config;

import com.deepfish.upload.services.AmazonS3StaticResourceResolver;
import com.deepfish.upload.services.AmazonS3UploadService;
import com.deepfish.upload.services.StaticResourceResolver;
import com.deepfish.upload.services.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile({"production", "staging", "dev"})
public class OnlineUploadConfiguration {

  @Bean
  S3Client s3Client(
      @Value("${aws.accessKeyId}") String accessKeyId,
      @Value("${aws.secretAccessKey}") String secretAccessKey) {
    return S3Client
        .builder()
        .region(Region.EU_WEST_3)
        .credentialsProvider(StaticCredentialsProvider
            .create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
        .build();
  }

  @Bean
  UploadService uploadService(S3Client s3Client,
      @Value("${deepfish.aws.s3.bucket-name}") String bucketName) {
    return new AmazonS3UploadService(s3Client, bucketName);
  }

  @Bean
  StaticResourceResolver staticResourceResolver(
      @Value("${deepfish.aws.s3.bucket-name}") String bucketName) {
    return new AmazonS3StaticResourceResolver(bucketName);
  }
}
