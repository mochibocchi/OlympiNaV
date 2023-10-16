package com.example.olympinav.generators;

import androidx.annotation.Nullable;

import com.example.olympinav.models.PlanTripType;
import com.example.olympinav.models.TransportationMethod;
import com.example.olympinav.models.TransportationMethodType;
import com.example.olympinav.models.Trip;
import com.example.olympinav.models.Vehicle;
import com.example.olympinav.models.LatLng;
import com.example.olympinav.models.NoiseLevel;
import com.example.olympinav.models.UsedCapacity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
  public static Vehicle generateVehicle(TransportationMethodType type, @Nullable LocalDateTime onwardsFrom) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    NoiseLevel n = NoiseLevel.values()[r.nextInt(NoiseLevel.values().length)];
    UsedCapacity c = UsedCapacity.values()[r.nextInt(UsedCapacity.values().length)];
    LatLng l = generateLatLng();
    List<LatLng> route = generateRoute(9, type);
    if (onwardsFrom == null) onwardsFrom = LocalDateTime.now();
    LocalDateTime startTime = generateLocalDateTime(onwardsFrom);
    LocalDateTime endTime = generateLocalDateTime(startTime);
    return new Vehicle(l, route, type, startTime, endTime, n, c);
  }

  public static TransportationMethod generateWalk(@Nullable LocalDateTime onwardsFrom) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    LatLng l = generateLatLng();
    List<LatLng> route = generateRoute(2, TransportationMethodType.WALK);
    if (onwardsFrom == null) onwardsFrom = LocalDateTime.now();
    LocalDateTime startTime = generateLocalDateTime(onwardsFrom);
    LocalDateTime endTime = generateLocalDateTime(startTime);
    return new TransportationMethod(l, route, TransportationMethodType.WALK, startTime, endTime);
  }

  public static LatLng generateLatLng() {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    double lat = r.nextDouble(-27.808466, -27.307661);
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
    return onwardsFrom.plusMinutes(r.nextInt(5, 30));
  }

  public static LocalDateTime generateLocalDateTimeBackwards(LocalDateTime backwardsFrom) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    return backwardsFrom.minusMinutes(r.nextInt(5, 30));
  }

  public static Trip generateTripForwards(LocalDateTime tripDepartAt) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    LatLng startLocation = generateLatLng();
    LatLng endLocation = generateLatLng();
    List<TransportationMethod> vehicles = new ArrayList<>();
    vehicles.add(generateWalk(tripDepartAt));
    LocalDateTime departAt = vehicles.get(0).getBoardAt();
    for (int i = 0; i < 3; i++) {
      TransportationMethodType type =
          TransportationMethodType.values()[r.nextInt(TransportationMethodType.values().length - 1)];
      vehicles.add(generateVehicle(type, vehicles.get(vehicles.size() - 1).getDepartAt()));
    }
    vehicles.add(generateWalk(vehicles.get(vehicles.size() - 1).getDepartAt()));
    LocalDateTime arriveAt = vehicles.get(vehicles.size() - 1).getDepartAt();
    return new Trip(startLocation, endLocation, departAt, arriveAt, vehicles);
  }

  public static Trip generateTripBackwards(LocalDateTime tripArriveAt) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    LatLng startLocation = generateLatLng();
    LatLng endLocation = generateLatLng();
    List<TransportationMethod> vehicles = new ArrayList<>();
    TransportationMethod walk = generateWalk(tripArriveAt);
    walk.setDepartAt(tripArriveAt);
    walk.setBoardAt(generateLocalDateTimeBackwards(walk.getDepartAt()));
    vehicles.add(walk);
    LocalDateTime departAt = vehicles.get(0).getBoardAt();
    for (int i = 0; i < 3; i++) {
      TransportationMethodType type =
          TransportationMethodType.values()[r.nextInt(TransportationMethodType.values().length - 1)];
      Vehicle v = generateVehicle(type, vehicles.get(vehicles.size() - 1).getDepartAt());
      v.setDepartAt(vehicles.get(vehicles.size() - 1).getBoardAt());
      v.setBoardAt(generateLocalDateTimeBackwards(v.getDepartAt()));
      vehicles.add(v);
    }
    walk = generateWalk(vehicles.get(vehicles.size() - 1).getDepartAt());
    walk.setDepartAt(vehicles.get(vehicles.size() - 1).getBoardAt());
    walk.setBoardAt(generateLocalDateTimeBackwards(walk.getDepartAt()));
    vehicles.add(walk);
    LocalDateTime arriveAt = vehicles.get(vehicles.size() - 1).getDepartAt();
    List<TransportationMethod> reversed = new ArrayList<>(vehicles.size());
    for (int i = vehicles.size() - 1; i >= 0; i--)
      reversed.add(vehicles.get(i));
    return new Trip(startLocation, endLocation, arriveAt, departAt, reversed);
  }
}
