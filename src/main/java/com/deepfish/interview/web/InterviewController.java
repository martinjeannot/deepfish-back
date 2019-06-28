package com.deepfish.interview.web;

import com.deepfish.interview.domain.Interview;
import com.deepfish.interview.repositories.InterviewRepository;
import com.deepfish.interview.services.InterviewService;
import com.deepfish.rest.util.ResourceList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RepositoryRestController
@RequestMapping("/interviews")
public class InterviewController {

  private static final Logger LOGGER = LoggerFactory.getLogger(InterviewController.class);

  private final InterviewService interviewService;

  private final InterviewRepository interviewRepository;

  private final EntityLinks entityLinks;

  public InterviewController(
      InterviewService interviewService,
      InterviewRepository interviewRepository,
      EntityLinks entityLinks
  ) {
    this.interviewService = interviewService;
    this.interviewRepository = interviewRepository;
    this.entityLinks = entityLinks;
  }

  @PostMapping("/create-resources")
  public ResponseEntity scheduleInterviews(
      @RequestBody Resource<ResourceList<Interview>> interviewResources
  ) {
    Iterable<Interview> interviews = interviewResources.getContent().getResources().stream()
        .map(Resource::getContent).collect(Collectors.toList());
    if (interviews.iterator().hasNext()) {
      interviews = interviewService.scheduleInterviews(interviews);
      return ResponseEntity
          .created(entityLinks.linkForSingleResource(interviews.iterator().next()).toUri())
          .build();
    }
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/admin/create-resources")
  public ResponseEntity scheduleInterviewsAsAdmin(
      @RequestBody Resource<ResourceList<Interview>> interviewResources
  ) {
    Iterable<Interview> interviews = interviewResources.getContent().getResources().stream()
        .map(Resource::getContent).collect(Collectors.toList());
    if (interviews.iterator().hasNext()) {
      interviews = interviewService.scheduleInterviewsAsAdmin(interviews);
      return ResponseEntity
          .created(entityLinks.linkForSingleResource(interviews.iterator().next()).toUri())
          .build();
    }
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{interviewId}/cancel")
  public ResponseEntity cancelInterview(
      @PathVariable("interviewId") UUID interviewId,
      @RequestBody Map<String, Object> body
  ) {
    Interview interview = interviewRepository.findOne(interviewId);
    if (Objects.isNull(interview)) {
      return ResponseEntity.notFound().build();
    }
    interviewService.cancelInterview(
        interview,
        body.get("cancelledBy").toString(),
        (Boolean) body.get("cancelLinkedInterviews")
    );
    return ResponseEntity.ok().build();
  }
}
