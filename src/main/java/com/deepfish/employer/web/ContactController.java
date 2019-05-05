package com.deepfish.employer.web;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.employer.forms.ContactForm;
import com.deepfish.employer.repositories.EmployerRepository;
import com.deepfish.employer.repositories.RequirementRepository;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@Validated
public class ContactController {

  private EmployerRepository employerRepository;

  private TalentRepository talentRepository;

  private RequirementRepository requirementRepository;

  private MailFactory mailFactory;

  private MailService mailService;

  public ContactController(
      EmployerRepository employerRepository,
      TalentRepository talentRepository,
      RequirementRepository requirementRepository,
      MailFactory mailFactory,
      MailService mailService
  ) {
    this.employerRepository = employerRepository;
    this.talentRepository = talentRepository;
    this.requirementRepository = requirementRepository;
    this.mailFactory = mailFactory;
    this.mailService = mailService;
  }

  @PostMapping("employers/contact")
  @ResponseBody
  public ResponseEntity sendMessageToAdmins(@Valid @RequestBody ContactForm contactForm) {
    Employer employer = employerRepository.findOne(contactForm.getEmployerId());
    Talent talent = talentRepository.findOne(contactForm.getTalentId());
    mailService
        .send(mailFactory
            .getAdminEmployerRequestMail(employer, talent, contactForm.getMessage()));
    return ResponseEntity.ok().build();
  }

  @PostMapping("employers/follow-up")
  @ResponseBody
  public ResponseEntity sendFollowUpMessageToAdmins(@Valid @RequestBody ContactForm contactForm) {
    Employer employer = employerRepository.findOne(contactForm.getEmployerId());
    Talent talent = talentRepository.findOne(contactForm.getTalentId());
    mailService
        .send(mailFactory
            .getAdminEmployerFollowUpMail(employer, talent, contactForm.getMessage()));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/requirements/{requirementId}/send-update")
  @ResponseBody
  public ResponseEntity sendRequirementUpdateMessageToAdmins(
      @PathVariable("requirementId") UUID requirementId,
      @RequestBody Map<String, Object> body
  ) {
    Requirement requirement = requirementRepository.findOne(requirementId);
    mailService
        .send(mailFactory
            .getAdminEmployerRequirementUpdateMail(requirement, body.get("message").toString()));
    return ResponseEntity.ok().build();
  }
}
