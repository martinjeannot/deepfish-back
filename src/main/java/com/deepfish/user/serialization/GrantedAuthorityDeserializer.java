package com.deepfish.user.serialization;

import com.deepfish.security.Role;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.core.GrantedAuthority;

@JsonComponent
public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {

  @Override
  public GrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    return Role.valueOf(node.get("authority").asText()).toGrantedAuthority();
  }
}
