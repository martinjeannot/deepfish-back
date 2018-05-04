package com.deepfish.talent.domain;

import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TalentFactory.class)
public interface TalentMapper {

  TalentMapper INSTANCE = Mappers.getMapper(TalentMapper.class);

  @Mapping(expression = "java(basicProfile)", target = "basicProfile")
  @Mapping(expression = "java(String.valueOf(basicProfile.get(\"firstName\")))", target = "firstName")
  @Mapping(expression = "java(String.valueOf(basicProfile.get(\"lastName\")))", target = "lastName")
  @Mapping(expression = "java(String.valueOf(basicProfile.get(\"emailAddress\")))", target = "email")
  Talent mapToTalent(Map basicProfile);
}
