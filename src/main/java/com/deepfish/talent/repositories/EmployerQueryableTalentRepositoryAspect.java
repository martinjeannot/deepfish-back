package com.deepfish.talent.repositories;

import com.deepfish.talent.domain.QEmployerQueryableTalent;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Since we cannot rely on client-side to filter out offline profiles, we cannot override
 * QueryDslPredicateExecutor to add any conditions to the predicate and we cannot user Spring
 * Security to postFilter offline profiles, we have to resort to AOP. See
 * https://stackoverflow.com/a/48843827
 */
@Aspect
@Component
public class EmployerQueryableTalentRepositoryAspect {

  @Pointcut("execution(* com.deepfish.talent.repositories.EmployerQueryableTalentRepository.findAll(com.querydsl.core.types.Predicate, org.springframework.data.domain.Pageable))")
  public void employerQueryableTalentFindAllWithPredicateAndPageable() {
  }

  @Around("employerQueryableTalentFindAllWithPredicateAndPageable()")
  public Object filterModelsByFlag(final ProceedingJoinPoint pjp) throws Throwable {
    Object[] args = pjp.getArgs();
    Predicate predicate = (Predicate) args[0];

    BooleanExpression flagIsTrue = QEmployerQueryableTalent.employerQueryableTalent.online.eq(true);

    if (predicate == null) {
      args[0] = flagIsTrue;
    } else {
      if (!predicate.toString().contains("employerQueryableTalent.online")) {
        args[0] = flagIsTrue.and(predicate);
      }
    }

    return pjp.proceed(args);
  }
}
