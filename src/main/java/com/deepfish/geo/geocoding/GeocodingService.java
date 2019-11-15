package com.deepfish.geo.geocoding;

public interface GeocodingService {

  <T> T geocode(String address, Class<T> returnType);
}
