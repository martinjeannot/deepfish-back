package com.deepfish.employer.web;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.forms.ContactForm;
import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import javax.validation.Valid;
import org.simplejavamail.email.Email;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@Validated
public class ContactController {

  private EmployerRepository employerRepository;

  private TalentRepository talentRepository;

  private MailFactory mailFactory;

  private MailService mailService;

  public ContactController(
      EmployerRepository employerRepository,
      TalentRepository talentRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    this.employerRepository = employerRepository;
    this.talentRepository = talentRepository;
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @PostMapping("employers/contact")
  @ResponseBody
  public ResponseEntity sendMessageToAdmins(@Valid @RequestBody ContactForm contactForm) {
    Employer employer = employerRepository.findOne(contactForm.getEmployerId());
    Talent talent = talentRepository.findOne(contactForm.getTalentId());
    Email adminEmployerRequestMail = mailFactory
        .getAdminEmployerRequestMail(employer, talent, contactForm.getMessage());
    mailService.send(adminEmployerRequestMail);
    return ResponseEntity.ok(null);
  }
}
