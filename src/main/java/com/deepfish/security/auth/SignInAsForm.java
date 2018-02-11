package com.deepfish.security.auth;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SignInAsForm {

  @NotBlank
  private String username;
}
