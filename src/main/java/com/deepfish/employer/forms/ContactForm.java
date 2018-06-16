package com.deepfish.employer.forms;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class ContactForm {

  @NotNull
  private UUID employerId;

  @NotNull
  private UUID talentId;

  @NotBlank
  private String message;
}
