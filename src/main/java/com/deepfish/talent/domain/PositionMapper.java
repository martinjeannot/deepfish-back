package com.deepfish.talent.domain;

import com.deepfish.company.domain.CompanyMapper;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CompanyMapper.class})
public interface PositionMapper {

  PositionMapper INSTANCE = Mappers.getMapper(PositionMapper.class);

  @Mapping(expression = "java(Long.valueOf((int) map.get(\"id\")))", target = "linkedInId")
  @Mapping(expression = "java(String.valueOf(map.get(\"title\")))", target = "title")
  @Mapping(expression = "java(String.valueOf(map.get(\"summary\")))", target = "summary")
  @Mapping(expression = "java(String.valueOf(map.get(\"startDate\")))", target = "startDate")
  @Mapping(expression = "java(String.valueOf(map.get(\"endDate\")))", target = "endDate")
  @Mapping(expression = "java(Boolean.valueOf((boolean) map.get(\"isCurrent\")))", target = "current")
  @Mapping(expression = "java(String.valueOf(((Map) map.get(\"location\")).get(\"name\")))", target = "locationName")
  @Mapping(expression = "java(String.valueOf(((Map) ((Map) map.get(\"location\")).get(\"country\")).get(\"code\")))", target = "locationCountryCode")
  @Mapping(expression = "java(com.deepfish.company.domain.CompanyMapper.INSTANCE.mapToCompany((Map) map.get(\"company\")))", target = "company")
  Position mapToPosition(Map map);
}
