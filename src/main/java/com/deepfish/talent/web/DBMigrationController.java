package com.deepfish.talent.web;

import com.deepfish.company.domain.CompanyMaturityLevel;
import com.deepfish.company.repositories.CompanyMaturityLevelRepository;
import com.deepfish.security.Role;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.domain.TalentMaturityLevel;
import com.deepfish.talent.domain.conditions.CommodityType;
import com.deepfish.talent.domain.conditions.Conditions;
import com.deepfish.talent.domain.conditions.FixedLocation;
import com.deepfish.talent.domain.conditions.Job;
import com.deepfish.talent.domain.conditions.TaskType;
import com.deepfish.talent.domain.qualification.Qualification;
import com.deepfish.talent.repositories.CommodityTypeRepository;
import com.deepfish.talent.repositories.FixedLocationRepository;
import com.deepfish.talent.repositories.JobRepository;
import com.deepfish.talent.repositories.TalentRepository;
import com.deepfish.talent.repositories.TaskTypeRepository;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBMigrationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(DBMigrationController.class);

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TalentRepository talentRepository;

  @Autowired
  private FixedLocationRepository fixedLocationRepository;

  @Autowired
  private CompanyMaturityLevelRepository companyMaturityLevelRepository;

  @Autowired
  private CommodityTypeRepository commodityTypeRepository;

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private TaskTypeRepository taskTypeRepository;

  @PostMapping("db/migration")
  @ResponseBody
  public ResponseEntity migrate() {
    Resource resource = new ClassPathResource("dbmigration.json");

    TaskType gestionTeam = taskTypeRepository
        .findOne(UUID.fromString("24f737e9-da1c-4251-8f47-7cd9f637fd8e"));
    TaskType accountManagement = taskTypeRepository
        .findOne(UUID.fromString("0a423494-e99a-4e00-8d06-7807a40dd056"));

    try {
      JsonParser jsonParser = new JsonFactory().createParser(resource.getInputStream());
      jsonParser.setCodec(objectMapper);
      Map data = jsonParser.readValueAs(Map.class);
      List<Map> users = (List<Map>) data.get("data");

      for (Map user : users) {
        Talent talent = new Talent();
        talent.setAuthorities(
            Arrays.asList(Role.ROLE_USER.toGrantedAuthority(),
                Role.ROLE_TALENT.toGrantedAuthority()));
        talent.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        talent.setCreatedAt(
            LocalDateTime.ofEpochSecond(
                Long.valueOf((Integer) ((Map) user.get("createdAt")).get("timestamp")),
                0,
                ZoneOffset.ofTotalSeconds((Integer) ((Map) user.get("createdAt")).get("offset"))
            )
        );
        talent.setLastSignedInAt(
            LocalDateTime.ofEpochSecond(
                Long.valueOf((Integer) ((Map) user.get("lastSignedInAt")).get("timestamp")),
                0,
                ZoneOffset
                    .ofTotalSeconds((Integer) ((Map) user.get("lastSignedInAt")).get("offset"))
            )
        );
        talent.setPhoneNumber("null");
        if (user.get("phoneNumber") != null
            && !user.get("phoneNumber").toString().isEmpty()) {
          talent.setPhoneNumber((String) user.get("phoneNumber"));
        }
        talent.setUsername((String) user.get("linkedInId"));
        talent.setLinkedInId((String) user.get("linkedInId"));
        talent.setEmail((String) user.get("email"));
        talent.setActive((Boolean) user.get("active"));
        // PROFIL
        talent.setFirstName((String) user.get("firstName"));
        talent.setLastName((String) user.get("lastName"));
        talent.setYearsOfExperience((Integer) user.get("yearsOfExperience"));
        talent.setSelfPitch((String) user.get("selfPitch"));
        if (user.get("maturityLevel") != null) {
          talent.setMaturityLevel(TalentMaturityLevel.valueOf((String) user.get("maturityLevel")));
        }

        // QUALIFICATION
        Qualification qualification = new Qualification();
        qualification.setComplexSellingSkillsRating(
            (Integer) user.get("complexSellingSkillsRating"));
        qualification.setHuntingSkillsRating((Integer) user.get("huntingSkillsRating"));
        qualification.setTechnicalSkillsRating((Integer) user.get("technicalSkillsRating"));
        qualification.setRecommendation((String) user.get("recommendation"));
        talent.setQualification(qualification);

        // CONDITIONS
        Conditions conditions = new Conditions();
        conditions.setFixedSalary(new BigDecimal((Integer) user.get("fixedSalary")));
        if (user.get("canStartOn") != null) {
          conditions.setCanStartOn(
              LocalDateTime.ofEpochSecond(
                  Long.valueOf((Integer) ((Map) user.get("canStartOn")).get("timestamp")),
                  0,
                  ZoneOffset
                      .ofTotalSeconds((Integer) ((Map) user.get("canStartOn")).get("offset"))
              ).toLocalDate()
          );
        }

        // COMPANY MATURITY LEVELS
        List<String> companyMaturityLevels = (List<String>) user.get("companyMaturityLevels");
        for (String maturityLevelId : companyMaturityLevels) {
          CompanyMaturityLevel companyMaturityLevel = companyMaturityLevelRepository
              .findOne(UUID.fromString(maturityLevelId));
          conditions.getCompanyMaturityLevels().add(companyMaturityLevel);
        }

        // COMMODITY TYPES
        List<String> commodityTypes = (List<String>) user.get("commodityTypes");
        for (String typeId : commodityTypes) {
          CommodityType commodityType = commodityTypeRepository.findOne(UUID.fromString(typeId));
          conditions.getCommodityTypes().add(commodityType);
        }

        // JOBS
        List<String> jobs = (List<String>) user.get("jobs");
        for (String jobId : jobs) {
          Job job = jobRepository.findOne(UUID.fromString(jobId));
          conditions.getJobs().add(job);
        }

        // TASK TYPES
        if (Objects.nonNull(user.get("add_gestion_team"))
            && Boolean.valueOf(true).equals(user.get("add_gestion_team"))) {
          conditions.getTaskTypes().add(gestionTeam);
        }
        if (Objects.nonNull(user.get("add_account_management"))
            && Boolean.valueOf(true).equals(user.get("add_account_management"))) {
          conditions.getTaskTypes().add(accountManagement);
        }

        // LOCATIONS
        List<String> fixedLocations = (List) user.get("fixedLocations");
        fixedLocations.removeIf(Objects::isNull);
        for (String fixedLocId : fixedLocations) {
          FixedLocation fixedLoc = fixedLocationRepository.findOne(UUID.fromString(fixedLocId));
          conditions.getFixedLocations().add(fixedLoc);
          if (fixedLoc.getParentLocation() == null) {
            List<FixedLocation> fixedLocations1 = fixedLocationRepository
                .findAllByParentLocation(fixedLoc);
            conditions.getFixedLocations().addAll(fixedLocations1);
          }
        }

        talent.setConditions(conditions);

        // Profile MAP
        Map<String, Object> profileMap = new HashMap<>();
        profileMap.put("email", user.get("email"));
        profileMap.put("id", user.get("linkedInId"));
        profileMap.put("firstName", user.get("firstName"));
        profileMap.put("lastName", user.get("lastName"));
        profileMap
            .put("formattedName", user.get("firstName").toString() + " " + user.get("lastName"));
        profileMap.put("headline", "N/A");
        Map<String, Object> location = new HashMap<>();
        location.put("country", new HashMap<>().put("code", "N/A"));
        location.put("name", "N/A");
        profileMap.put("location", location);
        profileMap.put("industry", "N/A");
        profileMap.put("numConnections", 0);
        profileMap.put("numConnectionsCapped", false);
        profileMap.put("summary", "N/A");
        profileMap.put("pictureUrl", user.get("photo"));
        profileMap.put("publicProfileUrl", user.get("lienLinkedIn"));
        Map<String, Object> positions = new HashMap<>();
        positions.put("_total", 0);
        positions.put("values", Collections.emptyList());
        profileMap.put("positions", positions);
        talent.setProfile(profileMap);

        talentRepository.save(talent);
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    return ResponseEntity.ok().build();
  }
}
