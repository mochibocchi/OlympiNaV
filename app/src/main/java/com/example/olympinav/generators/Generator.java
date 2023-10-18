package com.example.olympinav.generators;

import androidx.annotation.Nullable;

import com.example.olympinav.models.TravelMethod;
import com.example.olympinav.models.TravelType;
import com.example.olympinav.models.Trip;
import com.example.olympinav.models.LatLng;
import com.example.olympinav.models.NoiseLevel;
import com.example.olympinav.models.UsedCapacity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
  public static TravelMethod generateTransportationMethod(TravelType type, @Nullable LocalDateTime onwardsFrom) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    NoiseLevel n = NoiseLevel.values()[r.nextInt(NoiseLevel.values().length)];
    UsedCapacity c = UsedCapacity.values()[r.nextInt(UsedCapacity.values().length)];
    LatLng l = generateLatLng();
    List<LatLng> route = generateRoute(9, type);
    if (onwardsFrom == null) onwardsFrom = LocalDateTime.now();
    LocalDateTime startTime = generateLocalDateTime(onwardsFrom);
    LocalDateTime endTime = generateLocalDateTime(startTime);
    String routeNumber = String.valueOf(r.nextInt(111, 999));
    return new TravelMethod(l, route, type, startTime, endTime, n, c, type != TravelType.WALK ? routeNumber : null);
  }

  public static LatLng generateLatLng() {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    double lat = r.nextDouble(-27.808466, -27.307661);
    double lng = r.nextDouble(152.619324, 153.290863);
    return new LatLng(lat, lng);
  }

  public static List<LatLng> generateRoute(int length, TravelType type) {
    List<LatLng> route = new ArrayList<>(10);
    for (int i = 0; i < length; i++)
      route.add(generateLatLng());
    if (type != TravelType.WALK)
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

  // Used when searching for trips with the search methods 'Now' or 'Depart At'
  public static Trip generateTripForwards(LocalDateTime tripDepartAt) {
      ThreadLocalRandom r = ThreadLocalRandom.current();
      LatLng startLocation = generateLatLng();
      LatLng endLocation = generateLatLng();
      List<TravelMethod> vehicles = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
          // Start and end with walks, in between is a random type
          TravelType type = i == 0 || i == 4
              ? TravelType.WALK
              : TravelType.values()[r.nextInt(TravelType.values().length - 1)];

          // Start with the tripDepartAt argument, then use the previous trips departAt.
          LocalDateTime onwardsFrom = i == 0 ? tripDepartAt : vehicles.get(vehicles.size() - 1).getArriveAt();
          vehicles.add(generateTransportationMethod(type, onwardsFrom));
      }
      LocalDateTime departAt = vehicles.get(0).getDepartAt();
      LocalDateTime arriveAt = vehicles.get(vehicles.size() - 1).getArriveAt();
      return new Trip(startLocation, endLocation, departAt, arriveAt, vehicles);
  }

  // Used when searching for trips with the search method 'Arrive At'
  public static Trip generateTripBackwards(LocalDateTime tripArriveAt) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    LatLng startLocation = generateLatLng();
    LatLng endLocation = generateLatLng();
    List<TravelMethod> vehicles = new ArrayList<>();
    // Generating the trip backwards and then reversing the list because
    for (int i = 0; i < 5; i++) {
        TravelType type = i == 0 || i == 4
            ? TravelType.WALK
            : TravelType.values()[r.nextInt(TravelType.values().length - 1)];

      LocalDateTime backwardsFrom = i == 0 ? tripArriveAt : vehicles.get(vehicles.size() - 1).getDepartAt();
      TravelMethod v = generateTransportationMethod(type, backwardsFrom);
      v.setArriveAt(backwardsFrom);
      v.setDepartAt(generateLocalDateTimeBackwards(backwardsFrom));
      vehicles.add(v);
    }

    List<TravelMethod> reversed = new ArrayList<>(vehicles.size());
    for (int i = vehicles.size() - 1; i >= 0; i--)
      reversed.add(vehicles.get(i));
    LocalDateTime arriveAt = reversed.get(0).getDepartAt();
    LocalDateTime departAt = reversed.get(vehicles.size() - 1).getArriveAt();
    return new Trip(startLocation, endLocation, arriveAt, departAt, reversed);
  }
}
