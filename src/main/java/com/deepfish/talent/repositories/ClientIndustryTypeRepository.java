package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.conditions.ClientIndustryType;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientIndustryTypeRepository extends
    PagingAndSortingRepository<ClientIndustryType, UUID> {

}
