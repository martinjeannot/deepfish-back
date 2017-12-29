package com.deepfish.employer.web;

import com.deepfish.company.domain.Company;
import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.forms.SignUpForm;
import com.deepfish.employer.forms.SignUpFormMapper;
import com.deepfish.employer.services.EmployerService;
import javax.validation.Valid;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@Validated
public class EmployerController {

  private final EmployerService employerService;

  private final RepositoryEntityLinks repositoryEntityLinks;

  public EmployerController(EmployerService employerService,
      RepositoryEntityLinks repositoryEntityLinks) {
    this.employerService = employerService;
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
    Employer employer = SignUpFormMapper.INSTANCE.signUpFormToEmployer(signUpForm);
    Company company = SignUpFormMapper.INSTANCE.signUpFormToCompany(signUpForm);
    employer.setCompany(company);
    employerService.signUp(employer);
    return ResponseEntity.created(repositoryEntityLinks.linkForSingleResource(employer).toUri())
        .build();
  }
}
