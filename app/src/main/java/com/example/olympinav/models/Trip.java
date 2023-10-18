package com.example.olympinav.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Trip implements Parcelable {
  private LatLng startLocation;
  private LatLng endLocation;
  private LocalDateTime departAt;
  private LocalDateTime arriveAt;
  private List<TravelMethod> travelMethods;

  public Trip(LatLng startLocation, LatLng endLocation, LocalDateTime departAt, LocalDateTime arriveAt, List<TravelMethod> travelMethods) {
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    this.departAt = departAt;
    this.arriveAt = arriveAt;
    this.travelMethods = travelMethods;
  }

  public LatLng getStartLocation() {
    return startLocation;
  }

  public void setStartLocation(LatLng startLocation) {
    this.startLocation = startLocation;
  }

  public LatLng getEndLocation() {
    return endLocation;
  }

  public void setEndLocation(LatLng endLocation) {
    this.endLocation = endLocation;
  }

  public LocalDateTime getDepartAt() {
    return departAt;
  }

  public void setDepartAt(LocalDateTime departAt) {
    this.departAt = departAt;
  }

  public LocalDateTime getArriveAt() {
    return arriveAt;
  }

  public void setArriveAt(LocalDateTime arriveAt) {
    this.arriveAt = arriveAt;
  }

  public List<TravelMethod> getTravelMethods() {
    return travelMethods;
  }

  public void setTravelMethods(List<TravelMethod> travelMethods) {
    this.travelMethods = travelMethods;
  }

  public int getDuration(ChronoUnit unit) {
    return (int) unit.between(departAt, arriveAt);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.startLocation, flags);
    dest.writeParcelable(this.endLocation, flags);
    dest.writeSerializable(this.departAt);
    dest.writeSerializable(this.arriveAt);
    dest.writeTypedList(this.travelMethods);
  }

  public void readFromParcel(Parcel source) {
    this.startLocation = source.readParcelable(LatLng.class.getClassLoader());
    this.endLocation = source.readParcelable(LatLng.class.getClassLoader());
    this.departAt = (LocalDateTime) source.readSerializable();
    this.arriveAt = (LocalDateTime) source.readSerializable();
    this.travelMethods = source.createTypedArrayList(TravelMethod.CREATOR);
  }

  protected Trip(Parcel in) {
    this.startLocation = in.readParcelable(LatLng.class.getClassLoader());
    this.endLocation = in.readParcelable(LatLng.class.getClassLoader());
    this.departAt = (LocalDateTime) in.readSerializable();
    this.arriveAt = (LocalDateTime) in.readSerializable();
    this.travelMethods = in.createTypedArrayList(TravelMethod.CREATOR);
  }

  public static final Creator<Trip> CREATOR = new Creator<Trip>() {
    @Override
    public Trip createFromParcel(Parcel source) {
      return new Trip(source);
    }

    @Override
    public Trip[] newArray(int size) {
      return new Trip[size];
    }
  };
}
