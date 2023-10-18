package com.example.olympinav.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Trip implements Parcelable {
  private LatLng startLocation;
  private LatLng endLocation;
  private LocalDateTime leaveAt;
  private LocalDateTime arriveAt;
  private List<TransportationMethod> iDontKnow;

  public Trip(LatLng startLocation, LatLng endLocation, LocalDateTime leaveAt, LocalDateTime arriveAt, List<TransportationMethod> iDontKnow) {
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    this.leaveAt = leaveAt;
    this.arriveAt = arriveAt;
    this.iDontKnow = iDontKnow;
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

  public LocalDateTime getLeaveAt() {
    return leaveAt;
  }

  public void setLeaveAt(LocalDateTime leaveAt) {
    this.leaveAt = leaveAt;
  }

  public LocalDateTime getArriveAt() {
    return arriveAt;
  }

  public void setArriveAt(LocalDateTime arriveAt) {
    this.arriveAt = arriveAt;
  }

  public List<TransportationMethod> getiDontKnow() {
    return iDontKnow;
  }

  public void setiDontKnow(List<TransportationMethod> iDontKnow) {
    this.iDontKnow = iDontKnow;
  }

  public int getDuration(ChronoUnit unit) {
    return (int) unit.between(leaveAt, arriveAt);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.startLocation, flags);
    dest.writeParcelable(this.endLocation, flags);
    dest.writeSerializable(this.leaveAt);
    dest.writeSerializable(this.arriveAt);
    dest.writeTypedList(this.iDontKnow);
  }

  public void readFromParcel(Parcel source) {
    this.startLocation = source.readParcelable(LatLng.class.getClassLoader());
    this.endLocation = source.readParcelable(LatLng.class.getClassLoader());
    this.leaveAt = (LocalDateTime) source.readSerializable();
    this.arriveAt = (LocalDateTime) source.readSerializable();
    this.iDontKnow = source.createTypedArrayList(TransportationMethod.CREATOR);
  }

  protected Trip(Parcel in) {
    this.startLocation = in.readParcelable(LatLng.class.getClassLoader());
    this.endLocation = in.readParcelable(LatLng.class.getClassLoader());
    this.leaveAt = (LocalDateTime) in.readSerializable();
    this.arriveAt = (LocalDateTime) in.readSerializable();
    this.iDontKnow = in.createTypedArrayList(TransportationMethod.CREATOR);
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
