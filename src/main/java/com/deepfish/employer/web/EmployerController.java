package com.deepfish.employer.web;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.EmployerMapper;
import com.deepfish.employer.forms.PasswordResetForm;
import com.deepfish.employer.forms.SignUpForm;
import com.deepfish.employer.services.EmployerService;
import com.deepfish.security.auth.JwtTokenForge;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@Validated
public class EmployerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmployerController.class);

  @Value("#{'${deepfish.front.host}' + ':' + '${deepfish.front.port}'}")
  private String deepfishFrontAddress;

  private final EmployerService employerService;

  private final JwtTokenForge jwtTokenForge;

  private final RepositoryEntityLinks repositoryEntityLinks;

  public EmployerController(
      EmployerService employerService,
      JwtTokenForge jwtTokenForge,
      RepositoryEntityLinks repositoryEntityLinks) {
    this.employerService = employerService;
    this.jwtTokenForge = jwtTokenForge;
    this.repositoryEntityLinks = repositoryEntityLinks;
  }

  @PostMapping("employers")
  @ResponseBody
  public ResponseEntity create(@Valid @RequestBody Employer employer) {
    employerService.create(employer);
    return ResponseEntity.created(repositoryEntityLinks.linkForSingleResource(employer).toUri())
        .build();
  }

  @PostMapping("employers/sign-up")
  @ResponseBody
  public ResponseEntity signUp(@Valid @RequestBody SignUpForm signUpForm) {
    Employer employer = EmployerMapper.INSTANCE.signUpFormToEmployer(signUpForm);
    employer = employerService.signUp(employer);

    // authenticate employer
    OAuth2AccessToken authToken = jwtTokenForge.forgeToken(employer);

    return ResponseEntity.ok(authToken);
  }

  @PostMapping("employers/password-reset")
  @ResponseBody
  public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetForm passwordResetForm) {
    boolean success = employerService.resetPassword(passwordResetForm.getEmail());
    return ResponseEntity.ok(success ? "OK" : "KO");
  }
}
