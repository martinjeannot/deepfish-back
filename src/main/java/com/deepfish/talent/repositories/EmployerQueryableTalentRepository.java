package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.EmployerQueryableTalent;
import com.deepfish.talent.domain.QEmployerQueryableTalent;
import com.querydsl.core.BooleanBuilder;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
@PreAuthorize("hasRole('EMPLOYER')")
public interface EmployerQueryableTalentRepository extends
    PagingAndSortingRepository<EmployerQueryableTalent, UUID>,
    QueryDslPredicateExecutor<EmployerQueryableTalent>,
    QuerydslBinderCustomizer<QEmployerQueryableTalent> {

  @Override
  default void customize(
      QuerydslBindings bindings,
      QEmployerQueryableTalent talent
  ) {
    // Company
    bindings.bind(talent.companyId)
        .first((path, companyId) -> new BooleanBuilder()
            .andNot(talent.conditions.companyBlacklist.any().id.eq(companyId)));
    // Employer
    bindings.bind(talent.employerId)
        .first((path, employerId) -> new BooleanBuilder()
            .andNot(talent.opportunities.any().employer.id.eq(employerId)));
    // Experience
    bindings.bind(talent.minExperience)
        .first((path, minExperience) -> talent.yearsOfExperience.goe(minExperience));
    bindings.bind(talent.maxExperience)
        .first((path, maxExperience) -> talent.yearsOfExperience.loe(maxExperience));
    // Base salary
    bindings.bind(talent.minBaseSalary)
        .first((path, minBaseSalary) -> talent.conditions.fixedSalary.goe(minBaseSalary));
    bindings.bind(talent.maxBaseSalary)
        .first((path, maxBaseSalary) -> talent.conditions.fixedSalary.loe(maxBaseSalary));
    // Fixed Locations
    bindings.bind(talent.conditions.fixedLocations)
        .first((path, fixedLocations) -> {
          BooleanBuilder predicate = new BooleanBuilder();
          fixedLocations.forEach(fixedLocation -> predicate.or(path.contains(fixedLocation)));
          return predicate;
        });
  }
}
