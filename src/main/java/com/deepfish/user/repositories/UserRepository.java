package com.deepfish.user.repositories;

import com.deepfish.user.domain.AbstractUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<AbstractUser, Long> {

}
