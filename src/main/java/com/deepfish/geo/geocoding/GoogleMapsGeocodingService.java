package com.deepfish.geo.geocoding;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapsGeocodingService implements GeocodingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleMapsGeocodingService.class);

  private final GeoApiContext geoApiContext;

  private final Gson gson;

  public GoogleMapsGeocodingService(
      @Value("${google.apis.maps.api-key}") String apiKey
  ) {
    geoApiContext = new GeoApiContext
        .Builder()
        .apiKey(apiKey)
        .build();
    gson = new GsonBuilder()
        .create();
  }

  @Override
  public <T> T geocode(String address, Class<T> returnType) {
    GeocodingResult[] results;
    try {
      results = GeocodingApi.geocode(geoApiContext, address).await();
    } catch (ApiException | InterruptedException | IOException e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
    if (results.length == 0) {
      return null;
    }
    if (Map.class.isAssignableFrom(returnType)) {
      return gson.fromJson(gson.toJson(results[0]), returnType);
    } else if (GeocodingResult.class.isAssignableFrom(returnType)) {
      return (T) results[0];
    } else {
      throw new IllegalArgumentException("Illegal return type : " + returnType.toString());
    }
  }
}
