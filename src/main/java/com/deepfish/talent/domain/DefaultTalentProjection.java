package com.deepfish.talent.domain;

import com.deepfish.user.domain.DefaultUserProjection;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.security.core.GrantedAuthority;

@Projection(name = "default", types = {Talent.class})
public interface DefaultTalentProjection {

  UUID getId();

  String getFirstName();

  String getLastName();

  Collection<? extends GrantedAuthority> getAuthorities();

  DefaultUserProjection getTalentAdvocate();
}
