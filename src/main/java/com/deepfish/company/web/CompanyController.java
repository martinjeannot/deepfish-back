package com.deepfish.company.web;

import com.deepfish.company.domain.Company;
import com.deepfish.company.repositories.CompanyRepository;
import com.deepfish.upload.services.UploadService;
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

  private final UploadService uploadService;

  public CompanyController(CompanyRepository companyRepository, UploadService uploadService) {
    this.companyRepository = companyRepository;
    this.uploadService = uploadService;
  }

  @PostMapping("companies/{companyId}/upload-logo")
  public ResponseEntity uploadLogo(@PathVariable("companyId") UUID companyId,
      @RequestPart("file") MultipartFile file) {
    Company company = companyRepository.findOne(companyId);
    String logoURI = company
        .buildLogoURI(StringUtils.getFilenameExtension(file.getOriginalFilename()));
    uploadService.upload(file, logoURI);
    company.setLogoURI(logoURI);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("companies/{companyId}/upload-logo")
  public ResponseEntity deleteLogo(@PathVariable("companyId") UUID companyId) {
    Company company = companyRepository.findOne(companyId);
    uploadService.delete(company.getLogoURI());
    company.setLogoURI(null);
    companyRepository.save(company);
    return ResponseEntity.ok().build();
  }
}
