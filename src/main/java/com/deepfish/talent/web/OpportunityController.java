package com.deepfish.talent.web;

import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.opportunity.OpportunityStatus;
import com.deepfish.talent.domain.opportunity.QOpportunity;
import com.deepfish.talent.repositories.OpportunityRepository;
import com.deepfish.talent.repositories.TalentRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.UUID;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class OpportunityController {

  private final OpportunityRepository opportunityRepository;

  private final TalentRepository talentRepository;

  public OpportunityController(
      OpportunityRepository opportunityRepository,
      TalentRepository talentRepository) {
    this.opportunityRepository = opportunityRepository;
    this.talentRepository = talentRepository;
  }

  @PostMapping("/talents/{talentId}/opportunities/bulk-refusal")
  @ResponseBody
  public ResponseEntity refuseInBulk(@PathVariable("talentId") UUID talentId) {
    // the number of pending opportunities per talent should not justify a batch update here
    QOpportunity opportunity = QOpportunity.opportunity;
    BooleanExpression fromTalent = opportunity.talent.id.eq(talentId);
    BooleanExpression isPending = opportunity.status.eq(OpportunityStatus.PENDING);
    opportunityRepository.findAll(fromTalent.and(isPending)).forEach(pendingOpportunity -> {
      pendingOpportunity.setStatus(OpportunityStatus.REFUSED);
      opportunityRepository.save(pendingOpportunity);
    });
    Talent talent = talentRepository.findOne(talentId);
    talent.deactivate();
    talentRepository.save(talent);
    return ResponseEntity.ok(null);
  }
}
