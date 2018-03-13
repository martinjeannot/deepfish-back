package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.QQueryableTalent;
import com.deepfish.talent.domain.QueryableTalent;
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
    // Fixed salary
    bindings.bind(talent.minFixedSalary)
        .first((path, value) -> talent.conditions.fixedSalary.goe(value));
    bindings.bind(talent.maxFixedSalary)
        .first((path, value) -> talent.conditions.fixedSalary.loe(value));
    // Fixed salary
    /*bindings.bind(talent.conditions.fixedSalary).all((path, value) -> {
      Iterator<? extends BigDecimal> iterator = value.iterator();
      if (value.size() == 1) {
        return path.goe(iterator.next());
      } else {
        return path.between(iterator.next(), iterator.next());
      }
    });*/
  }
}
