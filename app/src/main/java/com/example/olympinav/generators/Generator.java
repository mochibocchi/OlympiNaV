package com.example.olympinav.generators;

import com.example.olympinav.models.TransportationMethod;
import com.example.olympinav.models.TransportationMethodType;
import com.example.olympinav.models.Vehicle;
import com.example.olympinav.models.LatLng;
import com.example.olympinav.models.NoiseLevel;
import com.example.olympinav.models.UsedCapacity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
  public static Vehicle generateVehicle(TransportationMethodType type) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    NoiseLevel n = NoiseLevel.values()[r.nextInt(NoiseLevel.values().length)];
    UsedCapacity c = UsedCapacity.values()[r.nextInt(UsedCapacity.values().length)];
    LatLng l = generateLatLng();
    List<LatLng> route = generateRoute(9, type);
    LocalDateTime startTime = generateLocalDateTime(LocalDateTime.now());
    LocalDateTime endTime = generateLocalDateTime(startTime);
    return new Vehicle(l, route, type, startTime, endTime, n, c);
  }

  public static TransportationMethod generateWalk() {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    LatLng l = generateLatLng();
    List<LatLng> route = generateRoute(2, TransportationMethodType.WALK);
    LocalDateTime startTime = generateLocalDateTime(LocalDateTime.now());
    LocalDateTime endTime = generateLocalDateTime(startTime);
    return new TransportationMethod(l, route, TransportationMethodType.WALK, startTime, endTime);
  }

  public static LatLng generateLatLng() {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    double lat = r.nextDouble(-27.307661, -27.808466);
    double lng = r.nextDouble(152.619324, 153.290863);
    return new LatLng(lat, lng);
  }

  public static List<LatLng> generateRoute(int length, TransportationMethodType type) {
    List<LatLng> route = new ArrayList<>(10);
    for (int i = 0; i < length; i++)
      route.add(generateLatLng());
    if (type != TransportationMethodType.WALK)
      route.add(route.get(0));
    return route;
  }

  public static LocalDateTime generateLocalDateTime(LocalDateTime onwardsFrom) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    return onwardsFrom.plusMinutes(r.nextInt(5, 60));
  }
}
