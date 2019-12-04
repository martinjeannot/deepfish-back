package com.deepfish.employer.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.user.domain.DefaultUserProjection;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.security.core.GrantedAuthority;

@Projection(name = "default", types = {Employer.class})
public interface DefaultEmployerProjection {

  UUID getId();

  String getUsername();

  String getFirstName();

  String getLastName();

  String getPhoneNumber();

  Collection<? extends GrantedAuthority> getAuthorities();

  LocalDateTime getCreatedAt();

  LocalDateTime getLastSignedInAt();

  Company getCompany();

  DefaultUserProjection getClientExecutive();
}
