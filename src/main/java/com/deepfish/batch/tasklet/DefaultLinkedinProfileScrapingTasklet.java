package com.deepfish.batch.tasklet;

import com.deepfish.aws.s3.StaticResourceResolver;
import com.deepfish.aws.s3.api.S3APIClient;
import com.deepfish.phantombuster.api.PhantombusterAPIClient;
import com.deepfish.talent.domain.Talent;
import com.deepfish.talent.repositories.TalentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Profile({"production"})
public class DefaultLinkedinProfileScrapingTasklet implements LinkedinProfileScrapingTasklet {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultLinkedinProfileScrapingTasklet.class);

  private static final String HTTP_PATTERN = "(?i)(http|https):";

  private static final Pattern LINKEDIN_PROFILE_URL_PATTERN = Pattern
      .compile("^" + HTTP_PATTERN + "//www.linkedin.com/in/.*$");

  private final int linkedinProfileScraperAgentId;

  private final TalentRepository talentRepository;

  private final PhantombusterAPIClient phantombusterAPIClient;

  private final ObjectMapper objectMapper;

  private final S3APIClient s3APIClient;

  private final StaticResourceResolver staticResourceResolver;

  public DefaultLinkedinProfileScrapingTasklet(
      @Value("${phantombuster.agents.linkedin-profile-scraper.id:#{0}}") int linkedinProfileScraperAgentId,
      TalentRepository talentRepository,
      PhantombusterAPIClient phantombusterAPIClient,
      ObjectMapper objectMapper,
      S3APIClient s3APIClient,
      StaticResourceResolver staticResourceResolver
  ) {
    this.linkedinProfileScraperAgentId = linkedinProfileScraperAgentId;
    this.talentRepository = talentRepository;
    this.phantombusterAPIClient = phantombusterAPIClient;
    this.objectMapper = objectMapper;
    this.s3APIClient = s3APIClient;
    this.staticResourceResolver = staticResourceResolver;
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    List<Talent> talents = talentRepository
        .findFirst10ByActiveIsTrueAndLinkedinProfileLastRetrievalAttemptedAtIsNullAndLinkedinPublicProfileUrlIsNotNullOrderByCreatedAtDesc();

    // cleaning up inputs : any invalid linkedIn profile URL messes array ordering (because nothing gets returned)
    talents = talents
        .stream()
        .filter(talent -> LINKEDIN_PROFILE_URL_PATTERN.matcher(talent.getLinkedinPublicProfileUrl())
            .matches())
        .collect(Collectors.toList());

    List<Map<String, Object>> linkedinProfiles = phantombusterAPIClient
        .launchLinkedinProfileScraperAgent(
            linkedinProfileScraperAgentId,
            "result-object-with-output",
            talents);

    if (Objects.isNull(linkedinProfiles) || linkedinProfiles.isEmpty()) {
      return RepeatStatus.FINISHED; // an admin should have been warned at this point
    }

    for (int i = 0; i < talents.size(); i++) {
      Talent talent = talents.get(i);
      Map<String, Object> linkedinProfile = linkedinProfiles.get(i);
      String cleanedLinkedinPublicProfileUrl = talent.getLinkedinPublicProfileUrl()
          .replaceAll("/$", "");
      if ((
          linkedinProfile.containsKey("details") && ((Map) linkedinProfile.get("details"))
              .get("linkedinProfile").toString().replaceAll("/$", "")
              .equals(cleanedLinkedinPublicProfileUrl)
      ) || (
          linkedinProfile.containsKey("general") && ((Map) linkedinProfile.get("general"))
              .get("profileUrl").toString().replaceAll("/$", "")
              .equals(cleanedLinkedinPublicProfileUrl)
      )) {
        // checked : the current linkedIn profile retrieval attempt was for the current talent (trailing slashes removed)
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        talent.setLinkedinProfileLastRetrievalAttemptedAt(now);
        if (linkedinProfile.containsKey("general")) {
          // checked : the current linkedIn profile retrieval attempt was successful
          Map<String, Object> general = (Map<String, Object>) linkedinProfile.get("general");
          talent.setLinkedinProfileLastRetrievedAt(now);
          talent.setFullProfile(linkedinProfile);
          try {
            talent.setFullProfileText(objectMapper.writeValueAsString(linkedinProfile));
          } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
          }
          // save profile picture
          String savedImgUrl = general.get("savedImg").toString();
          try (
              BufferedInputStream inputStream = new BufferedInputStream(
                  new URL(savedImgUrl).openStream());
              ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
          ) {
            ByteStreams.copy(inputStream, outputStream);
            String profilePictureURI = talent
                .buildProfilePictureURI(StringUtils.getFilenameExtension(savedImgUrl));
            s3APIClient.put(profilePictureURI, outputStream.toByteArray());
            talent
                .setProfilePictureUrl(staticResourceResolver.resolve(profilePictureURI).toString());
          } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
          }
        }
      }
    }

    talentRepository.save(talents);

    return RepeatStatus.FINISHED;
  }
}
