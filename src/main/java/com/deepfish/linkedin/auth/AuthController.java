package com.deepfish.linkedin.auth;

import com.deepfish.linkedin.LinkedInUtils;
import com.deepfish.linkedin.api.LinkedInAPIClient;
import com.deepfish.linkedin.domain.LiteProfile;
import com.deepfish.security.SystemAuthentication;
import com.deepfish.security.auth.TokenFactory;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.services.TalentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

  private final LinkedInAPIClient linkedInAPIClient;

  private final TalentService talentService;

  private final TokenFactory tokenFactory;

  private final ObjectMapper objectMapper;

  public AuthController(
      LinkedInAPIClient linkedInAPIClient,
      TalentService talentService,
      TokenFactory tokenFactory,
      ObjectMapper objectMapper
  ) {
    this.linkedInAPIClient = linkedInAPIClient;
    this.talentService = talentService;
    this.tokenFactory = tokenFactory;
    this.objectMapper = objectMapper;
  }

  @RequestMapping("/callback")
  public String authCallback(
      @RequestParam("state") String stateString,
      @RequestParam("code") Optional<String> authorizationCode,
      @RequestParam("error") Optional<String> error,
      @RequestParam("error_description") Optional<String> errorDescription,
      RedirectAttributes redirectAttributes
  ) {
    Map<String, Object> state;
    try {
      state = objectMapper.readValue(stateString, Map.class);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
      return "redirect://" + deepfishFrontAddress + "/#/";
    }

    if (!authorizationCode.isPresent()) {
      return getErrorRedirectionPageUrl(state);
    }

    String accessToken;
    try {
      accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode.get());
    } catch (RestClientException e) {
      return getErrorRedirectionPageUrl(state);
    }
    linkedInAPIClient.setAccessToken(accessToken);

    Map<String, Object> liteProfileResponse;
    Map<String, Object> emailAddressResponse;
    try {
      liteProfileResponse = linkedInAPIClient.get(LinkedInUtils.LITE_PROFILE_URI);
      emailAddressResponse = linkedInAPIClient.get(LinkedInUtils.EMAIL_ADDRESS_URI);
    } catch (RestClientException e) {
      return getErrorRedirectionPageUrl(state);
    }

    LiteProfile liteProfile = new LiteProfile(liteProfileResponse);

    String emailAddress = String.valueOf(
        ((Map) ((Map) ((List) emailAddressResponse
            .get("elements"))
            .get(0))
            .get("handle~"))
            .get("emailAddress"));

    UUID utmId = Objects.nonNull(state.get("utm_id"))
        ? UUID.fromString(state.get("utm_id").toString())
        : null;

    // manually set system authentication to access protected repo
    SecurityContextHolder.getContext().setAuthentication(SystemAuthentication.getAuthentication());

    Talent talent = talentService.signInFromLinkedIn(liteProfile, emailAddress, utmId);

    // authenticate talent
    OAuth2AccessToken authToken = tokenFactory.createToken(talent);

    try {
      redirectAttributes.addAttribute("auth_token", objectMapper.writeValueAsString(authToken));
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      return getErrorRedirectionPageUrl(state);
    }

    return "redirect://" + deepfishFrontAddress + "/#/auth/callback";
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
      throw e;
    }
    return (String) response.get("access_token");
  }

  private String getErrorRedirectionPageUrl(Map<String, Object> state) {
    String origin = String.valueOf(state.get("origin"));
    switch (origin) {
      case "sign-in":
        return "redirect://" + deepfishFrontAddress + "/#/sign-in?error";
      case "sign-up":
        return "redirect://" + deepfishFrontAddress + "/#/sign-up?error";
      default:
        return "redirect://" + deepfishFrontAddress + "/#/";

    }
  }
}
