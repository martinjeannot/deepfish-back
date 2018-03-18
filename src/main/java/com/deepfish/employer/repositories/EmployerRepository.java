package com.deepfish.employer.repositories;

import com.deepfish.employer.domain.Employer;
import com.deepfish.employer.domain.QEmployer;
import com.querydsl.core.types.dsl.StringExpression;
import java.util.UUID;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EmployerRepository extends PagingAndSortingRepository<Employer, UUID>,
    QueryDslPredicateExecutor<Employer>,
    QuerydslBinderCustomizer<QEmployer> {

  Employer findByUsername(String username);

  @Override
  default void customize(QuerydslBindings bindings, QEmployer employer) {
    bindings.bind(employer.username).first(StringExpression::contains);
    bindings.bind(employer.firstName).first(StringExpression::contains);
    bindings.bind(employer.lastName).first(StringExpression::contains);
  }
}
