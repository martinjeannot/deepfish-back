package com.deepfish.aws.config;

import com.deepfish.aws.s3.LocalStaticResourceResolver;
import com.deepfish.aws.s3.StaticResourceResolver;
import com.deepfish.aws.s3.api.LocalS3APIClient;
import com.deepfish.aws.s3.api.S3APIClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class OfflineAWSConfiguration {

  @Bean
  S3APIClient s3APIClient() {
    return new LocalS3APIClient();
  }

  @Bean
  StaticResourceResolver staticResourceResolver() {
    return new LocalStaticResourceResolver();
  }
}
