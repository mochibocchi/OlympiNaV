package com.example.olympinav.models;

public class LatLng {
  final double latitude;
  final double longitude;

  public LatLng(double latitude, double longitude) {
    if (latitude < -90 || latitude > 90)
      throw new IllegalArgumentException("Latitude must be between -90 and +90");
    if (longitude < -180 || longitude > 180)
      throw new IllegalArgumentException("Longitude must be between -180 and +180");
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
