package com.example.olympinav.models;

import java.time.LocalDateTime;
import java.util.List;

public class Vehicle extends TransportationMethod {
  private NoiseLevel noiseLevel;
  private UsedCapacity usedCapacity;

  public Vehicle(LatLng latlng, List<LatLng> route, TransportationMethodType type,
                 LocalDateTime boardAt, LocalDateTime departAt, NoiseLevel noiseLevel,
                 UsedCapacity usedCapacity) {
    super(latlng, route, type, boardAt, departAt);
    this.noiseLevel = noiseLevel;
    this.usedCapacity = usedCapacity;
  }
}
