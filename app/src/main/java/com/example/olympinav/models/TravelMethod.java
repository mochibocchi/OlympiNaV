package com.example.olympinav.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.List;

public class TravelMethod implements Parcelable {
  protected LatLng latlng;
  protected List<LatLng> route;
  protected TravelType type;
  protected LocalDateTime departAt;
  protected LocalDateTime arriveAt;
  private NoiseLevel noiseLevel;
  private UsedCapacity usedCapacity;
  private String routeNumber;

  public TravelMethod(LatLng latlng, List<LatLng> route, TravelType type, LocalDateTime departAt,
                      LocalDateTime arriveAt, NoiseLevel noiseLevel, UsedCapacity usedCapacity, String routeNumber) {
    this.latlng = latlng;
    this.route = route;
    this.type = type;
    this.departAt = departAt;
    this.arriveAt = arriveAt;
    this.noiseLevel = noiseLevel;
    this.usedCapacity = usedCapacity;
    this.routeNumber = routeNumber;
  }

  public LatLng getLatlng() {
    return latlng;
  }

  public void setLatlng(LatLng latlng) {
    this.latlng = latlng;
  }

  public List<LatLng> getRoute() {
    return route;
  }

  public void setRoute(List<LatLng> route) {
    this.route = route;
  }

  public TravelType getType() {
    return type;
  }

  public void setType(TravelType type) {
    this.type = type;
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

  public NoiseLevel getNoiseLevel() {
    return noiseLevel;
  }

  public void setNoiseLevel(NoiseLevel noiseLevel) {
    this.noiseLevel = noiseLevel;
  }

  public UsedCapacity getUsedCapacity() {
    return usedCapacity;
  }

  public void setUsedCapacity(UsedCapacity usedCapacity) {
    this.usedCapacity = usedCapacity;
  }

  public String getRouteNumber() {
    return routeNumber;
  }

  public void setRouteNumber(String routeNumber) {
    this.routeNumber = routeNumber;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.latlng, flags);
    dest.writeTypedList(this.route);
    dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    dest.writeSerializable(this.departAt);
    dest.writeSerializable(this.arriveAt);
    dest.writeInt(this.noiseLevel == null ? -1 : this.noiseLevel.ordinal());
    dest.writeInt(this.usedCapacity == null ? -1 : this.usedCapacity.ordinal());
    dest.writeString(this.routeNumber);
  }

  public void readFromParcel(Parcel source) {
    this.latlng = source.readParcelable(LatLng.class.getClassLoader());
    this.route = source.createTypedArrayList(LatLng.CREATOR);
    int tmpType = source.readInt();
    this.type = tmpType == -1 ? null : TravelType.values()[tmpType];
    this.departAt = (LocalDateTime) source.readSerializable();
    this.arriveAt = (LocalDateTime) source.readSerializable();
    int tmpNoiseLevel = source.readInt();
    this.noiseLevel = tmpNoiseLevel == -1 ? null : NoiseLevel.values()[tmpNoiseLevel];
    int tmpUsedCapacity = source.readInt();
    this.usedCapacity = tmpUsedCapacity == -1 ? null : UsedCapacity.values()[tmpUsedCapacity];
    this.routeNumber = source.readString();
  }

  protected TravelMethod(Parcel in) {
    this.latlng = in.readParcelable(LatLng.class.getClassLoader());
    this.route = in.createTypedArrayList(LatLng.CREATOR);
    int tmpType = in.readInt();
    this.type = tmpType == -1 ? null : TravelType.values()[tmpType];
    this.departAt = (LocalDateTime) in.readSerializable();
    this.arriveAt = (LocalDateTime) in.readSerializable();
    int tmpNoiseLevel = in.readInt();
    this.noiseLevel = tmpNoiseLevel == -1 ? null : NoiseLevel.values()[tmpNoiseLevel];
    int tmpUsedCapacity = in.readInt();
    this.usedCapacity = tmpUsedCapacity == -1 ? null : UsedCapacity.values()[tmpUsedCapacity];
    this.routeNumber = in.readString();
  }

  public static final Creator<TravelMethod> CREATOR = new Creator<TravelMethod>() {
    @Override
    public TravelMethod createFromParcel(Parcel source) {
      return new TravelMethod(source);
    }

    @Override
    public TravelMethod[] newArray(int size) {
      return new TravelMethod[size];
    }
  };
}
