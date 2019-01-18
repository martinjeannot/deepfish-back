package com.deepfish.talent.domain.opportunity;

import com.deepfish.interview.domain.Interview;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;

// Isn't this advocating GraphQL API ?
@Projection(name = "talent-interviews", types = {Opportunity.class})
public interface TalentInterviewsOpportunityProjection extends TalentOpportunityProjection {

  Set<Interview> getInterviews();
}
