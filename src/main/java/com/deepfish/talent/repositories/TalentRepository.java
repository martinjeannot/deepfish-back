package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.QTalent;
import com.deepfish.talent.domain.Talent;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TalentRepository extends PagingAndSortingRepository<Talent, UUID>,
    QueryDslPredicateExecutor<Talent>, QuerydslBinderCustomizer<QTalent> {

  Talent findByLinkedInIdOrEmail(String linkedInId, String email);

  Talent findByUsername(String username);

  @Override
  default void customize(QuerydslBindings bindings, QTalent talent) {
    bindings.bind(talent.conditions.fixedSalary).all((path, value) -> {
      Iterator<? extends BigDecimal> iterator = value.iterator();
      if (value.size() == 1) {
        return path.goe(iterator.next());
      } else {
        return path.between(iterator.next(), iterator.next());
      }
    });
  }
}
