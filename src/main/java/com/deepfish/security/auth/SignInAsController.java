package com.deepfish.security.auth;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@RequestMapping("/auth/sign-in-as")
@Validated
public class SignInAsController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SignInAsController.class);

  private final UserDetailsService userDetailsService;

  private final JwtTokenForge jwtTokenForge;

  public SignInAsController(
      UserDetailsService userDetailsService,
      JwtTokenForge jwtTokenForge) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenForge = jwtTokenForge;
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity signInAs(@Valid @RequestBody SignInAsForm signInAsForm) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(signInAsForm.getUsername());

    OAuth2AccessToken authToken = jwtTokenForge.forgeToken(userDetails);

    return ResponseEntity.ok(authToken);
  }
}
