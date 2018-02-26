package com.deepfish.talent.domain.opportunity;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "full", types = {Opportunity.class})
public interface FullOpportunityProjection extends PartialOpportunityProjection {

  boolean isForwarded();
}
