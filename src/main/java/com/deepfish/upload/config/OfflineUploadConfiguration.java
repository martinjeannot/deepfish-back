package com.deepfish.upload.config;

import com.deepfish.upload.services.LocalStaticResourceResolver;
import com.deepfish.upload.services.LocalUploadService;
import com.deepfish.upload.services.StaticResourceResolver;
import com.deepfish.upload.services.UploadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class OfflineUploadConfiguration {

  @Bean
  UploadService uploadService() {
    return new LocalUploadService();
  }

  @Bean
  StaticResourceResolver staticResourceResolver() {
    return new LocalStaticResourceResolver();
  }
}
