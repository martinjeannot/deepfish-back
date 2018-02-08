package com.deepfish.talent.domain.profile;

import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TalentProfileMapper {

  TalentProfileMapper INSTANCE = Mappers.getMapper(TalentProfileMapper.class);

  @Mapping(expression = "java(String.valueOf(map.get(\"emailAddress\")))", target = "emailAddress")
  @Mapping(expression = "java(String.valueOf(map.get(\"id\")))", target = "linkedInId")
  @Mapping(expression = "java(String.valueOf(map.get(\"firstName\")))", target = "firstName")
  @Mapping(expression = "java(String.valueOf(map.get(\"lastName\")))", target = "lastName")
  @Mapping(expression = "java(String.valueOf(map.get(\"formattedName\")))", target = "formattedName")
  @Mapping(expression = "java(String.valueOf(map.get(\"headline\")))", target = "headline")
  @Mapping(expression = "java(String.valueOf(((Map) map.get(\"location\")).get(\"name\")))", target = "locationName")
  @Mapping(expression = "java(String.valueOf(((Map) ((Map) map.get(\"location\")).get(\"country\")).get(\"code\")))", target = "locationCountryCode")
  @Mapping(expression = "java(String.valueOf(map.get(\"industry\")))", target = "industry")
  @Mapping(expression = "java(Integer.valueOf((int) map.get(\"numConnections\")))", target = "numConnections")
  @Mapping(expression = "java(Boolean.valueOf((boolean) map.get(\"numConnectionsCapped\")))", target = "numConnectionsCapped")
  @Mapping(expression = "java(String.valueOf(map.get(\"summary\")))", target = "summary")
  @Mapping(expression = "java(String.valueOf(map.getOrDefault(\"specialties\", \"\")))", target = "specialties")
  @Mapping(expression = "java(java.util.Arrays.asList(PositionMapper.INSTANCE.mapToPosition(((java.util.List<Map>) ((Map) map.get(\"positions\")).get(\"values\")).get(0))))", target = "positions")
  @Mapping(expression = "java(String.valueOf(map.get(\"pictureUrl\")))", target = "pictureUrl")
  @Mapping(expression = "java(((java.util.List<String>) ((Map) map.get(\"pictureUrls\")).get(\"values\")).get(0))", target = "originalPictureUrl")
  @Mapping(expression = "java(String.valueOf(map.get(\"siteStandardProfileRequest\")))", target = "siteStandardProfileRequest")
  @Mapping(expression = "java(String.valueOf(map.get(\"apiStandardProfileRequest\")))", target = "apiStandardProfileRequest")
  @Mapping(expression = "java(String.valueOf(map.get(\"publicProfileUrl\")))", target = "publicProfileUrl")
  TalentProfile mapToTalentProfile(Map map);
}
