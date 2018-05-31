package com.deepfish.security.auth.linkedin;

import com.deepfish.security.SystemAuthentication;
import com.deepfish.security.auth.JwtTokenForge;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.services.TalentService;
import com.deepfish.talent.util.LinkedInUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping(value = "/auth/linkedin", method = RequestMethod.GET)
public class AuthController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();

  @Value("#{'${deepfish.front.host}' + ':' + '${deepfish.front.port}'}")
  private String deepfishFrontAddress;

  @Value("${linkedin.client.id}")
  private String linkedInClientId;

  @Value("${linkedin.client.secret}")
  private String linkedInClientSecret;

  private final TalentService talentService;

  private final JwtTokenForge jwtTokenForge;

  private final ObjectMapper objectMapper;

  public AuthController(
      TalentService talentService,
      JwtTokenForge jwtTokenForge,
      ObjectMapper objectMapper) {
    this.talentService = talentService;
    this.jwtTokenForge = jwtTokenForge;
    this.objectMapper = objectMapper;
  }

  @RequestMapping("/callback")
  public String authCallback(
      @RequestParam("state") String state,
      @RequestParam("code") Optional<String> authorizationCode,
      @RequestParam("error") Optional<String> error,
      @RequestParam("error_description") Optional<String> errorDescription,
      RedirectAttributes redirectAttributes) {
    if (!authorizationCode.isPresent()) {
      return "redirect://" + deepfishFrontAddress + "/#/";
    }

    // manually set system authentication to access protected repo
    SecurityContextHolder.getContext().setAuthentication(SystemAuthentication.getAuthentication());

    Map response;
    try {
      response = sendAuthenticatedRequestToLinkedInAPI(HttpMethod.GET,
          LinkedInUtils.EMAIL_PROFILE_URI, authorizationCode.get());
    } catch (RestClientException e) {
      if (e instanceof HttpClientErrorException) {
        LOGGER.error(((HttpClientErrorException) e).getResponseBodyAsString());
      }
      LOGGER.error(e.getMessage(), e);
      return "redirect://" + deepfishFrontAddress + "/#/";
    }

    Talent talent = talentService.signInFromLinkedin(response);

    // authenticate talent
    OAuth2AccessToken authToken = jwtTokenForge.forgeToken(talent);

    try {
      redirectAttributes.addAttribute("auth_token", objectMapper.writeValueAsString(authToken));
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return "redirect://" + deepfishFrontAddress + "/#/auth/callback";
  }

  private Map<String, Object> sendAuthenticatedRequestToLinkedInAPI(HttpMethod method, String uri,
      String authorizationCode) {
    String accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("x-li-format", "json");
    RequestEntity requestEntity = new RequestEntity(headers, method,
        URI.create("https://api.linkedin.com" + uri));
    return REST_TEMPLATE.exchange(requestEntity, Map.class).getBody();
  }

  private String exchangeAuthorizationCodeForAccessToken(String authorizationCode) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("code", authorizationCode);
    body.add("redirect_uri", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
    body.add("client_id", linkedInClientId);
    body.add("client_secret", linkedInClientSecret);
    HttpEntity<MultiValueMap> request = new HttpEntity<>(body, headers);
    Map response;
    try {
      response = REST_TEMPLATE
          .postForObject("https://www.linkedin.com/oauth/v2/accessToken", request, Map.class);
    } catch (RestClientException e) {
      LOGGER.error(e.getMessage(), e);
      return "redirect://" + deepfishFrontAddress + "/#/";
    }
    return (String) response.get("access_token");
  }
}
