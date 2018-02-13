package com.deepfish.talent.domain;

import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TalentFactory.class)
public interface TalentMapper {

  TalentMapper INSTANCE = Mappers.getMapper(TalentMapper.class);

  @Mapping(expression = "java(map)", target = "profile")
  @Mapping(expression = "java(String.valueOf(map.get(\"firstName\")))", target = "firstName")
  @Mapping(expression = "java(String.valueOf(map.get(\"lastName\")))", target = "lastName")
  @Mapping(expression = "java(String.valueOf(map.get(\"emailAddress\")))", target = "email")
  Talent mapToTalent(Map map);
}
