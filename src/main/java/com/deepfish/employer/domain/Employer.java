package com.deepfish.employer.domain;

import com.deepfish.company.domain.Company;
import com.deepfish.user.domain.User;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Employer extends User {

  @ManyToOne(cascade = CascadeType.ALL)
  private Company company;
}
