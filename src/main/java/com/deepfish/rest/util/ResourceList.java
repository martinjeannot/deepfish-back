package com.deepfish.rest.util;

import java.util.List;
import lombok.Data;
import org.springframework.hateoas.Resource;

/**
 * Wrapper class allowing collection POSTing
 *
 * See https://stackoverflow.com/questions/31374689/how-to-post-a-list-to-spring-data-rest
 */
@Data
public class ResourceList<T> {

  private List<Resource<T>> resources;
}
