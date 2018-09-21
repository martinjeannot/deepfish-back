package com.deepfish.employer.web;

import static org.springframework.util.StringUtils.isEmpty;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.EmployerMapper;
import com.deepfish.employer.forms.PasswordResetForm;
import com.deepfish.employer.forms.SignUpForm;
import com.deepfish.employer.services.EmployerService;
import com.deepfish.employer.services.RequirementService;
import com.deepfish.security.auth.JwtTokenForge;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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

  @Value("${deepfish.typeform.requirement.public.email}")
  private String emailTypeformId;

  @Value("${deepfish.typeform.requirement.public.firstName}")
  private String firstNameTypeformId;

  @Value("${deepfish.typeform.requirement.public.lastName}")
  private String lastNameTypeformId;

  @Value("${deepfish.typeform.requirement.public.phoneNumber}")
  private String phoneNumberTypeformId;

  @Value("${deepfish.typeform.requirement.public.company}")
  private String companyTypeformId;

  private final EmployerService employerService;

  private final RequirementService requirementService;

  private final JwtTokenForge jwtTokenForge;

  private final RepositoryEntityLinks repositoryEntityLinks;

  public EmployerController(
      EmployerService employerService,
      RequirementService requirementService,
      JwtTokenForge jwtTokenForge,
      RepositoryEntityLinks repositoryEntityLinks) {
    this.employerService = employerService;
    this.requirementService = requirementService;
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
    employer = employerService.signUp(employer, false);

    // authenticate employer
    OAuth2AccessToken authToken = jwtTokenForge.forgeToken(employer);

    return ResponseEntity.ok(authToken);
  }

  @PostMapping("employers/typeform")
  @ResponseBody
  public ResponseEntity signUpFromTypeform(@RequestBody Map<String, Object> typeform) {
    // flatten typeform's answers by id
    Map answers = ((List<Map>) ((Map) typeform.get("form_response")).get("answers")).stream()
        .collect(Collectors.toMap(x -> ((Map) x.get("field")).get("id"), Function.identity()));

    // create signup form
    SignUpForm signUpForm = new SignUpForm();
    String email = ((Map) answers.get(emailTypeformId)).get("email").toString();
    signUpForm.setEmail(email);
    String firstName = ((Map) answers.get(firstNameTypeformId)).get("text").toString();
    signUpForm.setFirstName(isEmpty(firstName) ? email : firstName);
    String lastName = ((Map) answers.get(lastNameTypeformId)).get("text").toString();
    signUpForm.setLastName(isEmpty(lastName) ? email : lastName);
    String phoneNumber = ((Map) answers.get(phoneNumberTypeformId)).get("text").toString();
    signUpForm.setPhoneNumber(isEmpty(phoneNumber) ? email : phoneNumber);
    String company = ((Map) answers.get(companyTypeformId)).get("text").toString();
    signUpForm.setCompanyName(isEmpty(company) ? email : company);

    // submit signup form
    Employer employer = EmployerMapper.INSTANCE.signUpFormToEmployer(signUpForm);
    employer = employerService.signUp(employer, true);

    // create and save new requirement
    requirementService.saveNewRequirementFromTypeform(typeform, employer);

    return ResponseEntity.ok().build();
  }

  @PostMapping("employers/password-reset")
  @ResponseBody
  public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetForm passwordResetForm) {
    boolean success = employerService.resetPassword(passwordResetForm.getEmail());
    return ResponseEntity.ok(success ? "OK" : "KO");
  }
}
