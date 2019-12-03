package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.EmployerQueryableTalent;
import com.deepfish.talent.domain.QEmployerQueryableTalent;
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
    // Experience
    bindings.bind(talent.minExperience)
        .first((path, value) -> talent.yearsOfExperience.goe(value));
    bindings.bind(talent.maxExperience)
        .first((path, value) -> talent.yearsOfExperience.loe(value));
    // Base salary
    bindings.bind(talent.minBaseSalary)
        .first((path, value) -> talent.conditions.fixedSalary.goe(value));
    bindings.bind(talent.maxBaseSalary)
        .first((path, value) -> talent.conditions.fixedSalary.loe(value));
  }
}
