package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.conditions.FixedLocation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FixedLocationRepository extends PagingAndSortingRepository<FixedLocation, UUID> {

  List<FixedLocation> findAllByParentLocation(FixedLocation fixedLocation);
}
