package com.deepfish.employer.domain;

import com.deepfish.employer.forms.SignUpForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployerMapper {

  EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

  @Mapping(source = "email", target = "username")
  @Mapping(expression = "java(com.deepfish.company.domain.CompanyMapper.INSTANCE.signUpFormToCompany(signUpForm))", target = "company")
  Employer signUpFormToEmployer(SignUpForm signUpForm);
}
