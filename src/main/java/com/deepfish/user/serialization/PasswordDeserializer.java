package com.deepfish.user.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordDeserializer extends JsonDeserializer<String> {

  private final PasswordEncoder passwordEncoder;

  public PasswordDeserializer(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return passwordEncoder.encode(p.getValueAsString());
  }
}
