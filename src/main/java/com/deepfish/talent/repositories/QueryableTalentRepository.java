package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.QQueryableTalent;
import com.deepfish.talent.domain.QueryableTalent;
import com.querydsl.core.BooleanBuilder;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
@PreAuthorize("hasRole('ADMIN')")
public interface QueryableTalentRepository extends
    PagingAndSortingRepository<QueryableTalent, UUID>,
    QueryDslPredicateExecutor<QueryableTalent>,
    QuerydslBinderCustomizer<QQueryableTalent> {

  @Override
  default void customize(QuerydslBindings bindings, QQueryableTalent talent) {
    // Keyword search
    bindings.bind(talent.searchQuery).first((path, searchQuery) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      Arrays.stream(searchQuery.split("\\s+"))
          .forEach(keyword -> predicate
              // TODO : implement posix regex matching with querydsl/hibernate/postgres
              .or(talent.basicProfileText.containsIgnoreCase(keyword))
              .or(talent.fullProfileText.containsIgnoreCase(keyword))
              .or(talent.selfPitch.containsIgnoreCase(keyword))
              .or(talent.notes.containsIgnoreCase(keyword))
              .or(talent.qualification.recommendation.containsIgnoreCase(keyword)));
      return predicate;
    });
    // Years of experience
    bindings.bind(talent.minYearsOfExperience).first(
        ((path, minYearsOfExperience) -> talent.yearsOfExperience.goe(minYearsOfExperience)));
    bindings.bind(talent.maxYearsOfExperience).first(
        ((path, maxYearsOfExperience) -> talent.yearsOfExperience.loe(maxYearsOfExperience)));
    // Company maturity levels
    bindings.bind(talent.conditions.companyMaturityLevels).first((path, companyMaturityLevels) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      companyMaturityLevels
          .forEach(companyMaturityLevel -> predicate.or(path.contains(companyMaturityLevel)));
      return predicate;
    });
    bindings.bind(talent.companyMaturityLevelsNotIn).first((path, companyMaturityLevels) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      companyMaturityLevels.forEach(companyMaturityLevel -> predicate
          .andNot(talent.conditions.companyMaturityLevels.contains(companyMaturityLevel)));
      return predicate;
    });
    // Job types
    bindings.bind(talent.conditions.jobTypes).first((path, jobTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      jobTypes.forEach(jobType -> predicate.or(path.contains(jobType)));
      return predicate;
    });
    // Commodity types
    bindings.bind(talent.conditions.commodityTypes).first((path, commodityTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      commodityTypes.forEach(commodityType -> predicate.or(path.contains(commodityType)));
      return predicate;
    });
    // Task types
    bindings.bind(talent.conditions.taskTypes).first((path, taskTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      taskTypes.forEach(taskType -> predicate.or(path.contains(taskType)));
      return predicate;
    });
    bindings.bind(talent.taskTypesNotIn).first(((path, taskTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      taskTypes
          .forEach(taskType -> predicate.andNot(talent.conditions.taskTypes.contains(taskType)));
      return predicate;
    }));
    // Fixed Locations
    bindings.bind(talent.conditions.fixedLocations).first((path, fixedLocations) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      fixedLocations.forEach(fixedLocation -> predicate.or(path.contains(fixedLocation)));
      return predicate;
    });
    bindings.bind(talent.fixedLocationsNotIn).first((path, fixedLocations) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      fixedLocations.forEach(fixedLocation -> predicate
          .andNot(talent.conditions.fixedLocations.contains(fixedLocation)));
      return predicate;
    });
    // Fixed salary
    bindings.bind(talent.minFixedSalary)
        .first((path, value) -> talent.conditions.fixedSalary.goe(value));
    bindings.bind(talent.maxFixedSalary)
        .first((path, value) -> talent.conditions.fixedSalary.loe(value));
  }
}
