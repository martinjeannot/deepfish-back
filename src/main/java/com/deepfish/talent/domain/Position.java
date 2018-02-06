package com.deepfish.talent.domain;

import com.deepfish.company.domain.Company;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"profile", "company"})
@EqualsAndHashCode(exclude = {"profile", "company"})
public class Position {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @ManyToOne
  @JsonBackReference
  private TalentProfile profile;

  @Column(unique = true)
  private long linkedInId;

  private String title;

  private String summary;

  private String startDate;

  private String endDate;

  private boolean current;

  private String locationName;

  private String locationCountryCode;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
  private Company company;
}
