package com.deepfish.security.auth.linkedin;

import com.deepfish.security.auth.JwtTokenForge;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import com.deepfish.talent.services.TalentService;
import com.deepfish.talent.util.LinkedInUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
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

  private final TalentService talentService;

  private final TalentRepository talentRepository;

  private final JwtTokenForge jwtTokenForge;

  private final ObjectMapper objectMapper;

  public AuthController(
      TalentService talentService,
      TalentRepository talentRepository,
      JwtTokenForge jwtTokenForge,
      ObjectMapper objectMapper) {
    this.talentService = talentService;
    this.talentRepository = talentRepository;
    this.jwtTokenForge = jwtTokenForge;
    this.objectMapper = objectMapper;
  }

  @RequestMapping("/sign-in")
  public String signInCallback(
      @RequestParam("state") String state,
      @RequestParam("code") Optional<String> authorizationCode,
      @RequestParam("error") Optional<String> error,
      @RequestParam("error_description") Optional<String> errorDescription,
      RedirectAttributes redirectAttributes) {
    if (!authorizationCode.isPresent()) {
      return "redirect:http://localhost:8081/#/";
    }

    Map response;
    try {
      response = sendAuthenticatedRequestToLinkedInAPI(HttpMethod.GET,
          LinkedInUtils.EMAIL_PROFILE_URI, authorizationCode.get());
    } catch (RestClientException e) {
      if (e instanceof HttpClientErrorException) {
        LOGGER.error(((HttpClientErrorException) e).getResponseBodyAsString());
      }
      LOGGER.error(e.getMessage(), e);
      return "redirect:http://localhost:8081/#/";
    }

    // check if talent exists
    Talent talent = talentRepository.findByLinkedInId((String) response.get("id"));
    if (talent == null) {
      // sign up
      return "redirect:http://localhost:8081/#/sign-up";
    }

    // authenticate existing talent
    OAuth2AccessToken authToken = jwtTokenForge.forgeToken(talent);

    try {
      redirectAttributes.addAttribute("auth_token", objectMapper.writeValueAsString(authToken));
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return "redirect:http://localhost:8081/#/auth/callback";
  }

  @RequestMapping("/sign-up")
  public String signUpCallback(
      @RequestParam("state") String state,
      @RequestParam("code") Optional<String> authorizationCode,
      @RequestParam("error") Optional<String> error,
      @RequestParam("error_description") Optional<String> errorDescription) {
    if (!authorizationCode.isPresent()) {
      return "redirect:http://localhost:8081/#/";
    }

    Map<String, Object> response;
    try {
      response = sendAuthenticatedRequestToLinkedInAPI(HttpMethod.GET,
          LinkedInUtils.EMAIL_PROFILE_URI, authorizationCode.get());
    } catch (RestClientException e) {
      LOGGER.error(e.getMessage(), e);
      return "redirect:http://localhost:8081/#/";
    }

    // check if talent does not already exist
    Talent talent = talentRepository.findByLinkedInId((String) response.get("id"));
    if (talent != null) {
      // signin
      return "redirect:http://localhost:8081/#/";
    }

    // register new talent
    talentService.signUpFromLinkedIn(response);

    // authenticate newly registered talent

    return "redirect:http://localhost:8081/#/";
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
    body.add("client_id", "77w79kdr6gql2h");
    body.add("client_secret", "7ovNCwpTojlQLQXI");
    HttpEntity<MultiValueMap> request = new HttpEntity<>(body, headers);
    Map response;
    try {
      response = REST_TEMPLATE
          .postForObject("https://www.linkedin.com/oauth/v2/accessToken", request, Map.class);
    } catch (RestClientException e) {
      LOGGER.error(e.getMessage(), e);
      return "redirect:http://localhost:8081/#/";
    }
    return (String) response.get("access_token");
  }
}
