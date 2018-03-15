package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.QQueryableTalent;
import com.deepfish.talent.domain.QueryableTalent;
import com.querydsl.core.BooleanBuilder;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface QueryableTalentRepository extends
    PagingAndSortingRepository<QueryableTalent, UUID>,
    QueryDslPredicateExecutor<QueryableTalent>,
    QuerydslBinderCustomizer<QQueryableTalent> {

  @Override
  default void customize(QuerydslBindings bindings, QQueryableTalent talent) {
    // Company maturity levels
    bindings.bind(talent.conditions.companyMaturityLevels).first((path, companyMaturityLevels) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      companyMaturityLevels
          .forEach(companyMaturityLevel -> predicate.and(path.contains(companyMaturityLevel)));
      return predicate;
    });
    bindings.bind(talent.companyMaturityLevelsNotIn).first((path, companyMaturityLevels) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      companyMaturityLevels.forEach(companyMaturityLevel -> predicate
          .andNot(talent.conditions.companyMaturityLevels.contains(companyMaturityLevel)));
      return predicate;
    });
    // Jobs
    bindings.bind(talent.conditions.jobs).first((path, jobs) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      jobs.forEach(job -> predicate.and(path.contains(job)));
      return predicate;
    });
    // Commodity types
    bindings.bind(talent.conditions.commodityTypes).first((path, commodityTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      commodityTypes.forEach(commodityType -> predicate.and(path.contains(commodityType)));
      return predicate;
    });
    // Task types
    bindings.bind(talent.conditions.taskTypes).first((path, taskTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      taskTypes.forEach(taskType -> predicate.and(path.contains(taskType)));
      return predicate;
    });
    // Fixed Locations
    bindings.bind(talent.conditions.fixedLocations).first((path, fixedLocations) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      fixedLocations.forEach(fixedLocation -> predicate.and(path.contains(fixedLocation)));
      return predicate;
    });
    // Fixed salary
    bindings.bind(talent.minFixedSalary)
        .first((path, value) -> talent.conditions.fixedSalary.goe(value));
    bindings.bind(talent.maxFixedSalary)
        .first((path, value) -> talent.conditions.fixedSalary.loe(value));
  }
}
