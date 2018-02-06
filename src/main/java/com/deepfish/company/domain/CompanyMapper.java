package com.deepfish.company.domain;

import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper {

  CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

  @Mapping(expression = "java(Long.valueOf((int) map.get(\"id\")))", target = "linkedInId")
  @Mapping(expression = "java(String.valueOf(map.get(\"name\")))", target = "name")
  @Mapping(expression = "java(String.valueOf(map.get(\"type\")))", target = "type")
  @Mapping(expression = "java(String.valueOf(map.get(\"industry\")))", target = "industry")
  @Mapping(expression = "java(String.valueOf(map.get(\"size\")))", target = "size")
  Company mapToCompany(Map map);
}
