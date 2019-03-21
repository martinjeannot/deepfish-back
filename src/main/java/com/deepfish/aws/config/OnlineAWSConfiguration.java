package com.deepfish.aws.config;

import com.deepfish.aws.s3.OnlineStaticResourceResolver;
import com.deepfish.aws.s3.StaticResourceResolver;
import com.deepfish.aws.s3.api.OnlineS3APIClient;
import com.deepfish.aws.s3.api.S3APIClient;
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
public class OnlineAWSConfiguration {

  @Bean
  S3Client s3Client(
      @Value("${aws.accessKeyId}") String accessKeyId,
      @Value("${aws.secretAccessKey}") String secretAccessKey
  ) {
    return S3Client
        .builder()
        .region(Region.EU_WEST_3)
        .credentialsProvider(StaticCredentialsProvider
            .create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
        .build();
  }

  @Bean
  S3APIClient s3APIClient(
      S3Client s3Client,
      @Value("${deepfish.aws.s3.bucket-name}") String bucketName
  ) {
    return new OnlineS3APIClient(s3Client, bucketName);
  }

  @Bean
  StaticResourceResolver staticResourceResolver(
      @Value("${deepfish.aws.s3.bucket-name}") String bucketName
  ) {
    return new OnlineStaticResourceResolver(bucketName);
  }
}
