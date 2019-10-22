package com.deepfish.core.web;

import com.google.common.base.CaseFormat;
import java.lang.reflect.Field;
import java.util.Objects;
import javax.persistence.EntityManager;

public abstract class AbstractStatisticsController {

  private final EntityManager entityManager;

  public AbstractStatisticsController(
      EntityManager entityManager
  ) {
    this.entityManager = entityManager;
  }

  protected String checkEventFieldName(Class clazz, String eventFieldName) {
    do {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.getName().equals(eventFieldName)) {
          return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
      }
      clazz = clazz.getSuperclass();
    } while (Objects.nonNull(clazz));

    throw new IllegalArgumentException("Cannot find field named : " + eventFieldName);
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
