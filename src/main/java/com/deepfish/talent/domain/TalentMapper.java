package com.deepfish.talent.domain;

import com.deepfish.linkedin.domain.LiteProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TalentFactory.class)
public interface TalentMapper {

  TalentMapper INSTANCE = Mappers.getMapper(TalentMapper.class);

  Talent liteProfileToTalent(LiteProfile liteProfile);
}
