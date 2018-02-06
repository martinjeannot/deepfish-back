package com.deepfish.talent.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TalentFactory.class)
public interface TalentMapper {

  TalentMapper INSTANCE = Mappers.getMapper(TalentMapper.class);

  @Mapping(source = "linkedInId", target = "username")
  @Mapping(source = "emailAddress", target = "email")
  Talent profileToTalent(TalentProfile profile);
}
