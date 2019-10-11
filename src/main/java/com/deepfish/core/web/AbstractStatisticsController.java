package com.deepfish.core.web;

import javax.persistence.EntityManager;

public abstract class AbstractStatisticsController {

  private final EntityManager entityManager;

  public AbstractStatisticsController(
      EntityManager entityManager
  ) {
    this.entityManager = entityManager;
  }

  protected String[] extractDatePatternAndInterval(String groupBy) {
    switch (groupBy) {
      case "day":
        return new String[]{"YYYY-MM-DD", "1 day"};
      case "week":
        return new String[]{"IYYY-IW", "1 week"};
      case "month":
        return new String[]{"YYYY-MM", "1 month"};
      case "year":
        return new String[]{"YYYY", "1 year"};
      default:
        throw new IllegalArgumentException("Unknown group-by value : " + groupBy);
    }
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }
}
