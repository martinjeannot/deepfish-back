package com.deepfish.user.converters;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthoritiesConverter implements
    AttributeConverter<Collection<? extends GrantedAuthority>, String> {

  @Override
  public String convertToDatabaseColumn(Collection<? extends GrantedAuthority> attribute) {
    return String
        .join(",", attribute.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
  }

  @Override
  public Collection<? extends GrantedAuthority> convertToEntityAttribute(String dbData) {
    return Arrays.stream(dbData.split(",")).map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }
}
