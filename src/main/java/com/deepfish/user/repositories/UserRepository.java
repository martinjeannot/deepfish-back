package com.deepfish.user.repositories;

import com.deepfish.user.domain.User;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

  User findByUsername(String username);
}
