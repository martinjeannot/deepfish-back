package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.Conditions;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "conditions", collectionResourceRel = "conditions")
public interface ConditionsRepository extends PagingAndSortingRepository<Conditions, UUID> {

}
