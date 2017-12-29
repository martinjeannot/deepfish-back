package com.deepfish.employer.forms;

import com.deepfish.company.domain.Company;
import com.deepfish.employer.domain.Employer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SignUpFormMapper {

  SignUpFormMapper INSTANCE = Mappers.getMapper(SignUpFormMapper.class);

  @Mapping(source = "email", target = "username")
  Employer signUpFormToEmployer(SignUpForm signUpForm);

  @Mapping(source = "companyName", target = "name")
  Company signUpFormToCompany(SignUpForm signUpForm);
}
