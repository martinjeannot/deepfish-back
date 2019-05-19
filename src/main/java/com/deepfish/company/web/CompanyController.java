package com.deepfish.company.web;

import com.deepfish.aws.s3.api.S3APIClient;
import com.deepfish.company.domain.Company;
import com.deepfish.company.repositories.CompanyRepository;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RepositoryRestController
public class CompanyController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

  private final CompanyRepository companyRepository;

  private final S3APIClient s3APIClient;

  public CompanyController(
      CompanyRepository companyRepository,
      S3APIClient s3APIClient
  ) {
    this.companyRepository = companyRepository;
    this.s3APIClient = s3APIClient;
  }

  @PostMapping("companies/{companyId}/upload-logo")
  public ResponseEntity uploadLogo(
      @PathVariable("companyId") UUID companyId,
      @RequestPart("file") MultipartFile file
  ) {
    Company company = companyRepository.findOne(companyId);
    String resourceUri =
        company
            .buildLogoUri(
                StringUtils.getFilenameExtension(
                    file.getOriginalFilename()));
    uploadResource(resourceUri, file);
    company.setLogoURI(resourceUri);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("companies/{companyId}/upload-logo")
  public ResponseEntity deleteLogo(
      @PathVariable("companyId") UUID companyId
  ) {
    Company company = companyRepository.findOne(companyId);
    s3APIClient.delete(company.getLogoURI());
    company.setLogoURI(null);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @PostMapping("companies/{companyId}/upload-cover")
  public ResponseEntity uploadCoverImage(
      @PathVariable("companyId") UUID companyId,
      @RequestPart("file") MultipartFile file
  ) {
    Company company = companyRepository.findOne(companyId);
    String resourceUri =
        company
            .buildCoverImageUri(
                StringUtils.getFilenameExtension(
                    file.getOriginalFilename()));
    uploadResource(resourceUri, file);
    company.setCoverImageUri(resourceUri);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("companies/{companyId}/upload-cover")
  public ResponseEntity deleteCoverImage(
      @PathVariable("companyId") UUID companyId
  ) {
    Company company = companyRepository.findOne(companyId);
    s3APIClient.delete(company.getCoverImageUri());
    company.setCoverImageUri(null);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @PostMapping("companies/{companyId}/upload-top")
  public ResponseEntity uploadTopImage(
      @PathVariable("companyId") UUID companyId,
      @RequestPart("file") MultipartFile file
  ) {
    Company company = companyRepository.findOne(companyId);
    String resourceUri =
        company
            .buildTopImageUri(
                StringUtils.getFilenameExtension(
                    file.getOriginalFilename()));
    uploadResource(resourceUri, file);
    company.setTopImageUri(resourceUri);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("companies/{companyId}/upload-top")
  public ResponseEntity deleteTopImage(
      @PathVariable("companyId") UUID companyId
  ) {
    Company company = companyRepository.findOne(companyId);
    s3APIClient.delete(company.getTopImageUri());
    company.setTopImageUri(null);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @PostMapping("companies/{companyId}/upload-bottom")
  public ResponseEntity uploadBottomImage(
      @PathVariable("companyId") UUID companyId,
      @RequestPart("file") MultipartFile file
  ) {
    Company company = companyRepository.findOne(companyId);
    String resourceUri =
        company
            .buildBottomImageUri(
                StringUtils.getFilenameExtension(
                    file.getOriginalFilename()));
    uploadResource(resourceUri, file);
    company.setBottomImageUri(resourceUri);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("companies/{companyId}/upload-bottom")
  public ResponseEntity deleteBottomImage(
      @PathVariable("companyId") UUID companyId
  ) {
    Company company = companyRepository.findOne(companyId);
    s3APIClient.delete(company.getBottomImageUri());
    company.setBottomImageUri(null);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  private void uploadResource(String resourceUri, MultipartFile file) {
    try {
      s3APIClient.put(resourceUri, file.getBytes());
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }
}
