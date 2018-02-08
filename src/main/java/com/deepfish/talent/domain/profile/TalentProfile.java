package com.deepfish.talent.domain.profile;

import com.deepfish.talent.domain.Talent;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"talent", "positions"})
@EqualsAndHashCode(exclude = {"talent", "positions"})
public class TalentProfile {

  @Id
  @Setter(AccessLevel.NONE)
  private UUID id;

  @MapsId
  @OneToOne
  private Talent talent;

  private String emailAddress;

  @Column(unique = true)
  private String linkedInId;

  private String firstName;

  private String lastName;

  private String formattedName;

  private String headline;

  private String locationName;

  private String locationCountryCode;

  private String industry;

  private int numConnections;

  private boolean numConnectionsCapped;

  private String summary;

  private String specialties;

  @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Position> positions;

  private String pictureUrl;

  private String originalPictureUrl;

  private String siteStandardProfileRequest;

  private String apiStandardProfileRequest;

  private String publicProfileUrl;

  public void synchronizePositions() {
    positions.forEach(position -> position.setProfile(this));
  }
}
