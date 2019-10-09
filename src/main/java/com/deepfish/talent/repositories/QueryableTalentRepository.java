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
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.ast.Literal;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.expression.spel.ast.OperatorNot;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
@PreAuthorize("hasRole('ADMIN')")
public interface QueryableTalentRepository extends
    PagingAndSortingRepository<QueryableTalent, UUID>,
    QueryDslPredicateExecutor<QueryableTalent>,
    QuerydslBinderCustomizer<QQueryableTalent> {

  ExpressionParser expressionParser = new SpelExpressionParser();

  @Override
  default void customize(QuerydslBindings bindings, QQueryableTalent talent) {
    // Keyword search
    bindings.bind(talent.searchQuery).first((path, searchQuery) -> {
      Expression expression = expressionParser.parseExpression(searchQuery);
      return traverseAST(((SpelExpression) expression).getAST(), talent);
    });
    // Requirements
    bindings.bind(talent.requirementIdsNotIn).first((path, requirementIds) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      requirementIds.forEach(requirementId -> predicate
          .andNot(talent.opportunities.any().requirement.id.eq(requirementId)));
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
    // Industry types
    bindings.bind(talent.conditions.industryTypes).first((path, industryTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      industryTypes.forEach(industryType -> predicate.or(path.contains(industryType)));
      return predicate;
    });
    // Client industry types
    bindings.bind(talent.conditions.clientIndustryTypes).first((path, clientIndustryTypes) -> {
      BooleanBuilder predicate = new BooleanBuilder();
      clientIndustryTypes
          .forEach(clientIndustryType -> predicate.or(path.contains(clientIndustryType)));
      return predicate;
    });
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

  /**
   * Traverse the Abstract Syntax Tree generated by Spring
   *
   * @param node the node to traverse
   * @param talent needed for leaf node to generate the search predicate
   * @return the traversed tree as a {@link BooleanBuilder}
   */
  default BooleanBuilder traverseAST(SpelNode node, QQueryableTalent talent) {
    if (node.getChildCount() == 0) {
      if (Literal.class.isAssignableFrom(node.getClass())) {
        return getSearchPredicateFor(((Literal) node).getLiteralValue().getValue().toString(),
            talent);
      } else {
        return getSearchPredicateFor(node.toStringAST(), talent);
      }
    } else if (node.getChildCount() == 1) {
      if (OperatorNot.class.isAssignableFrom(node.getClass())) {
        return traverseAST(node.getChild(0), talent).not();
      } else {
        throw new IllegalArgumentException("Unknown unary operator : " + node.getClass());
      }
    } else {
      BooleanBuilder predicate = new BooleanBuilder();
      for (int i = 0; i < node.getChildCount(); i++) {
        if (OpAnd.class.isAssignableFrom(node.getClass())) {
          predicate.and(traverseAST(node.getChild(i), talent));
        } else if (OpOr.class.isAssignableFrom(node.getClass())) {
          predicate.or(traverseAST(node.getChild(i), talent));
        } else {
          throw new IllegalArgumentException("Unknown operator : " + node.getClass());
        }
      }
      return predicate;
    }
  }

  /**
   * Helper method to generate the search predicate for the given word(s)
   *
   * @param value the string to search
   * @param talent the QModel to search within
   * @return the search predicate as a {@link BooleanBuilder}
   */
  default BooleanBuilder getSearchPredicateFor(String value, QQueryableTalent talent) {
    return new BooleanBuilder()
        .or(talent.fullProfileText.containsIgnoreCase(value))
        .or(talent.selfPitch.containsIgnoreCase(value))
        .or(talent.notes.containsIgnoreCase(value))
        .or(talent.qualification.recommendation.containsIgnoreCase(value));
  }
}
