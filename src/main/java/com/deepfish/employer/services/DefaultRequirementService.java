package com.deepfish.employer.services;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.requirement.Requirement;
import com.deepfish.employer.repositories.RequirementRepository;
import com.deepfish.mail.MailFactory;
import com.deepfish.mail.MailService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * {@link RequirementService} default implementation
 */
@Service
public class DefaultRequirementService implements RequirementService {

  @Value("${deepfish.typeform.requirement.public.name}")
  private String requirementNamePublicTypeformId;

  @Value("${deepfish.typeform.requirement.private.name}")
  private String requirementNamePrivateTypeformId;

  private final RequirementRepository requirementRepository;

  private final MailService mailService;

  private final MailFactory mailFactory;

  public DefaultRequirementService(
      RequirementRepository requirementRepository,
      MailService mailService,
      MailFactory mailFactory) {
    this.requirementRepository = requirementRepository;
    this.mailService = mailService;
    this.mailFactory = mailFactory;
  }

  @Override
  public Requirement saveNewRequirementFromTypeform(Map<String, Object> typeform,
      Employer creator) {
    // build new requirement
    Requirement requirement = new Requirement();
    requirement.setCreatedBy(creator.getId());
    requirement.setCompany(creator.getCompany());
    Optional<Map> requirementNameAnswer =
        ((List<Map>) ((Map) typeform.get("form_response")).get("answers"))
            .stream()
            .filter(answer -> {
              String answerId = ((Map) answer.get("field")).get("id").toString();
              return answerId.equals(requirementNamePublicTypeformId)
                  || answerId.equals(requirementNamePrivateTypeformId);
            })
            .findFirst();
    String requirementName =
        requirementNameAnswer.isPresent() ? requirementNameAnswer.get().get("text").toString()
            : "Mon nouveau besoin";
    requirement.setName(requirementName);
    requirement.setTypeform(typeform);

    // save new requirement
    requirement = requirementRepository.save(requirement);

    // notify admins
    mailService.send(mailFactory.getAdminNewRequirementMail(requirement));

    return requirement;
  }
}
