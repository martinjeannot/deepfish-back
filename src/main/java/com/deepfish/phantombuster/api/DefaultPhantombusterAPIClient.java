package com.deepfish.phantombuster.api;

import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DefaultPhantombusterAPIClient implements PhantombusterAPIClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPhantombusterAPIClient.class);

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();

  private static final String SESSION_COOKIE = "AQEDARMQIMcDq3q7AAABbTEu160AAAFtVTtbrU0AcWtgDzwG9h0jmu0es3pkh30b1gwbAtKHIS3zlHd8RoZPsspqzx4xLABDk9k1w_IeqtIpm0dIg5ZU9aoc643k5Kvq8Q6b2xHYF4DGTRsu3igthRH8";

  private final String apiKey;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultPhantombusterAPIClient(
      @Value("${phantombuster.client.api-key:#{null}}") String apiKey,
      MailService mailService,
      MailFactory mailFactory
  ) {
    this.apiKey = apiKey;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public List<Map<String, Object>> launchLinkedinProfileScraperAgent(
      int id,
      String output,
      List<Talent> talents
  ) {
    // build argument param
    Map<String, Object> argument = new HashMap<>();
    argument.put("sessionCookie", SESSION_COOKIE);
    argument.put("emailChooser", "none");
    argument.put("saveImg", true);
    argument.put("takeScreenshot", false);
    argument.put("takePartialScreenshot", false);

    if (Objects.nonNull(talents) && !talents.isEmpty()) {
      argument.put("numberOfAddsPerLaunch", talents.size());
      argument.put("profileUrls",
          talents.stream().map(Talent::getLinkedinPublicProfileUrl).collect(Collectors.toList()));
      argument.put("noDatabase", true);
    }

    Map<String, Object> data = launchAgent(id, output, argument);

    if (Objects.isNull(data)) {
      return null;
    }
    return (List<Map<String, Object>>) data.get("resultObject");
  }

  @Override
  public Map<String, Object> launchAgent(
      int id,
      String output,
      Map<String, Object> argument
  ) {
    // building request URI
    Map<String, Object> uriVariables = new HashMap<>();
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/agent/{id}/launch");
    uriVariables.put("id", id);

    // building request body
    Map<String, Object> body = new HashMap<>();
    body.put("output", output);
    body.put("argument", argument);

    Map<String, Object> response = sendAuthenticatedRequestToPhantombusterAPI(
        HttpMethod.POST,
        uriComponentsBuilder.buildAndExpand(uriVariables).toUriString(),
        body);

    if (Objects.isNull(response)) {
      return null;
    }
    return (Map<String, Object>) response.get("data");
  }

  private Map<String, Object> sendAuthenticatedRequestToPhantombusterAPI(
      HttpMethod method,
      String uri,
      Map<String, Object> body
  ) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("X-Phantombuster-Key-1", apiKey);
    if (Objects.nonNull(body)) {
      headers.setContentType(MediaType.APPLICATION_JSON);
    }

    RequestEntity requestEntity = new RequestEntity<>(
        body,
        headers,
        method,
        URI.create("https://phantombuster.com/api/v1" + uri));
    Map<String, Object> response;
    try {
      response = REST_TEMPLATE.exchange(requestEntity, Map.class).getBody();
    } catch (RestClientException e) {
      LOGGER.error(e.getMessage(), e);
      mailService.send(mailFactory.getAdminBatchWarningMail(e.getMessage()));
      return null;
    }

    if (Objects.nonNull(response) && "success".equals(response.get("status"))) {
      return response;
    } else if (Objects.isNull(response)) {
      mailService.send(mailFactory.getAdminBatchWarningMail("NULL response"));
    } else if (response.containsKey("data") && ((Map) response.get("data")).containsKey("output")) {
      mailService.send(mailFactory
          .getAdminBatchWarningMail(((Map) response.get("data")).get("output").toString()));
    } else {
      mailService.send(mailFactory.getAdminBatchWarningMail("N/A"));
    }
    return null;
  }
}
