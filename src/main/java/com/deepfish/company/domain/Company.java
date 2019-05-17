package com.deepfish.company.domain;

import com.deepfish.employer.domain.Employer;
import com.querydsl.core.annotations.QueryEntity;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@QueryEntity
@Data
public class Company {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private UUID id;

  @NotNull
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now(Clock.systemUTC());

  @NotBlank
  private String name;

  @NotNull
  @OneToMany(mappedBy = "company")
  private Set<Employer> employers = new HashSet<>();

  @NotNull
  @Column(columnDefinition = "text")
  private String description = "";

  @Column(name = "logo_uri")
  private String logoURI;

  private String websiteUrl;

  private String size;

  private String headquartersAddress;

  private String foundedIn;

  private String revenue;

  private String customerReferences;

  private String coverImageUri;

  private String topImageUri;

  private String bottomImageUri;

  private String facebookUrl;

  private String instagramUrl;

  private String linkedinUrl;

  private String twitterUrl;

  private String youtubeUrl;

  // ===============================================================================================

  public String buildLogoURI(String extension) {
    return "/companies/" + id.toString() + "/logo." + extension;
  }
}
