package com.deepfish.linkedin.api;

import java.util.Map;
import org.springframework.web.client.RestClientException;

public interface LinkedInAPIClient {

  Map<String, Object> get(String uri) throws RestClientException;

  void setAccessToken(String accessToken);
}
