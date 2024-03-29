package com.deepfish.company.domain;

import com.deepfish.employer.forms.SignUpForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

  CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

  @Mapping(source = "companyName", target = "name")
  Company signUpFormToCompany(SignUpForm signUpForm);
}
