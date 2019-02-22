package com.deepfish.linkedin.api;

import java.net.URI;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class DefaultLinkedInAPIClient implements LinkedInAPIClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLinkedInAPIClient.class);

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();

  @Value("${linkedin.client.id}")
  private String linkedInClientId;

  @Value("${linkedin.client.secret}")
  private String linkedInClientSecret;

  private String accessToken;

  @Override
  public Map<String, Object> get(String uri) throws RestClientException {
    return sendAuthenticatedRequestToLinkedInAPI(HttpMethod.GET, uri);
  }

  private Map<String, Object> sendAuthenticatedRequestToLinkedInAPI(
      HttpMethod method,
      String uri
  ) throws RestClientException {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("x-li-format", "json");
    RequestEntity requestEntity = new RequestEntity(
        headers,
        method,
        URI.create("https://api.linkedin.com" + uri));
    try {
      return REST_TEMPLATE.exchange(requestEntity, Map.class).getBody();
    } catch (RestClientException e) {
      if (e instanceof HttpClientErrorException) {
        LOGGER.error(((HttpClientErrorException) e).getResponseBodyAsString());
      }
      LOGGER.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
