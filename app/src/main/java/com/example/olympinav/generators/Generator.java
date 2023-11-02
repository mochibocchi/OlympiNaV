package com.example.olympinav.generators;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.example.olympinav.R;
import com.example.olympinav.models.ServiceUpdate;
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

// This class generates random data in the place of real data received from cloud servers and sensors. The data is
// used to simulate what the app would look like if were using real data.
public class Generator {

  // Generates a random TravelMethod, which in this application represents time on a public transport, such as a bus,
  // train or ferry.
  public static TravelMethod generateTravelMethod(TravelType type, @Nullable LocalDateTime onwardsFrom) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    NoiseLevel n = NoiseLevel.values()[r.nextInt(NoiseLevel.values().length)];
    UsedCapacity c = UsedCapacity.values()[r.nextInt(UsedCapacity.values().length)];
    LatLng l = generateLatLng();
    List<LatLng> route = generateRoute(9, type);
    if (onwardsFrom == null) onwardsFrom = LocalDateTime.now();
    LocalDateTime startTime = generateLocalDateTime(onwardsFrom, true);
    LocalDateTime endTime = generateLocalDateTime(startTime, false);
    String routeNumber = String.valueOf(r.nextInt(111, 999));
    return new TravelMethod(l, route, type, startTime, endTime, n, c, type != TravelType.WALK ? routeNumber : null);
  }

  // Generates a Latitude and Longitude somewhere in Brisbane.
  public static LatLng generateLatLng() {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    double lat = r.nextDouble(-27.808466, -27.307661);
    double lng = r.nextDouble(152.619324, 153.290863);
    return new LatLng(lat, lng);
  }

  // Generates a route of random LatLngs in Brisbane. This is not used in the program, but would be used in the real
  // app to display the route of a bus or train to the user on the Map provided.
  public static List<LatLng> generateRoute(int length, TravelType type) {
    List<LatLng> route = new ArrayList<>(10);
    for (int i = 0; i < length; i++)
      route.add(generateLatLng());
    if (type != TravelType.WALK)
      route.add(route.get(0));
    return route;
  }

  // Generates a random LocalDateTime that is after the provided onwardsFrom date time. The isTransferTime parameter
  // is true if this LocalDateTime is between getting off one method of transport, and getting on the next. It is
  // false if this LocalDateTime is between getting on a method of transport, and getting off it. This was used to
  // fine tune the amount of time spent on a bus and the amount of time spent between two buses.
  public static LocalDateTime generateLocalDateTime(LocalDateTime onwardsFrom, boolean isTransferTime) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    return onwardsFrom.plusMinutes(r.nextInt(5, isTransferTime ? 10 : 30));
  }

  // Same os generateLocalDateTime() but generates a time before the provided backwardsFrom parameter. See
  // generateLocalDateTime() for description of isTransferTime parameter.
  public static LocalDateTime generateLocalDateTimeBackwards(LocalDateTime backwardsFrom, boolean isTransferTime) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    return backwardsFrom.minusMinutes(r.nextInt(5, isTransferTime ? 10 : 30));
  }

  // Generates a Trip, which in this application, is the journey from a start location, to an end location via public
  // transport. This method generates the trip forwards, starts at the beginning of the trip, and finishes at the end.
  public static Trip generateTripForwards(LocalDateTime tripDepartAt) {
      ThreadLocalRandom r = ThreadLocalRandom.current();
      LatLng startLocation = generateLatLng();
      LatLng endLocation = generateLatLng();

      List<TravelMethod> vehicles = new ArrayList<>();
      int options = r.nextInt(3, 5);
      for (int i = 0; i < options; i++) {
          // Start and end with walks, in between is a random type
          TravelType type = i == 0 || i == options - 1
              ? TravelType.WALK
              : TravelType.values()[r.nextInt(TravelType.values().length - 1)];

          // Start with the tripDepartAt argument, then use the previous trips departAt.
          LocalDateTime onwardsFrom = i == 0 ? tripDepartAt : vehicles.get(vehicles.size() - 1).getArriveAt();
          vehicles.add(generateTravelMethod(type, onwardsFrom));
      }
      LocalDateTime departAt = vehicles.get(0).getDepartAt();
      LocalDateTime arriveAt = vehicles.get(vehicles.size() - 1).getArriveAt();
      return new Trip(startLocation, endLocation, departAt, arriveAt, vehicles);
  }

  // Generates a Trip, which in this application, is the journey from a start location, to an end location via public
  // transport. This method generates the trip backwards, starts at the end of the trip, and finishes at the
  // beginning. This is used when the user searches for a trip using the 'Arrive At' option, providing the time they
  // wish to arrive at a destination. This forces us to work backwards.
  public static Trip generateTripBackwards(LocalDateTime tripArriveAt) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    LatLng startLocation = generateLatLng();
    LatLng endLocation = generateLatLng();
    List<TravelMethod> vehicles = new ArrayList<>();
    // Generating the trip backwards and then reversing the list because
    int options = r.nextInt(3, 5);
    for (int i = 0; i < options; i++) {
        TravelType type = i == 0 || i == options - 1
            ? TravelType.WALK
            : TravelType.values()[r.nextInt(TravelType.values().length - 1)];

      LocalDateTime backwardsFrom = i == 0 ? tripArriveAt : vehicles.get(vehicles.size() - 1).getDepartAt();
      TravelMethod v = generateTravelMethod(type, backwardsFrom);
      v.setArriveAt(backwardsFrom);
      v.setDepartAt(generateLocalDateTimeBackwards(backwardsFrom, true));
      vehicles.add(v);
    }

    List<TravelMethod> reversed = new ArrayList<>(vehicles.size());
    for (int i = vehicles.size() - 1; i >= 0; i--)
      reversed.add(vehicles.get(i));
    LocalDateTime arriveAt = reversed.get(0).getDepartAt();
    LocalDateTime departAt = reversed.get(vehicles.size() - 1).getArriveAt();
    return new Trip(startLocation, endLocation, arriveAt, departAt, reversed);
  }

  // Generates random Service Updates, there are tree types of service update, they follow a certain template and use
  // randomized bus numbers.
  public static ServiceUpdate generateServiceUpdate() {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    String routeNumber = String.valueOf(r.nextInt(111, 999));
    int type = r.nextInt(1, 4);
    LocalDateTime lastUpdatedAt = LocalDateTime.now().minusMinutes(r.nextInt(120));
    String message;
    @DrawableRes int drawable;
    if (type == 1) {
      drawable = R.drawable.baseline_info_24;
      message = "Timetable temporarily updated for " + routeNumber + " to improve accessibility for QUT students " +
          "during exam period.";
    } else if (type == 2) {
      drawable = R.drawable.baseline_warning_24;
      message = "Stops temporarily moved for " + routeNumber + " due to construction. Trip planner has been updated " +
          "accordingly.";
    } else {
      drawable = R.drawable.baseline_error_24;
      message = routeNumber + " will be at stopped 9:30pm on the 13th of November to perform maintenance. " +
          "It will resume service the following morning.";
    }
    return new ServiceUpdate(message, drawable, lastUpdatedAt);
  }
}
