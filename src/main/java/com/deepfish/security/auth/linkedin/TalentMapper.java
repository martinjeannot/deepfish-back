package com.deepfish.security.auth.linkedin;

import com.deepfish.talent.domain.Talent;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TalentFactory.class)
public interface TalentMapper {

  TalentMapper INSTANCE = Mappers.getMapper(TalentMapper.class);

  @Mappings({
      @Mapping(expression = "java(map.get(\"id\"))", target = "username"),
      @Mapping(expression = "java(map.get(\"firstName\"))", target = "firstName"),
      @Mapping(expression = "java(map.get(\"lastName\"))", target = "lastName"),
      @Mapping(expression = "java(map.get(\"emailAddress\"))", target = "linkedInEmail"),
      @Mapping(expression = "java(map.get(\"emailAddress\"))", target = "email"),
  })
  Talent mapToTalent(Map<String, String> map);
}
