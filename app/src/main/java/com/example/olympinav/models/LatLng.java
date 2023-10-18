package com.example.olympinav.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LatLng implements Parcelable {
  double latitude;
  double longitude;

  public LatLng(double latitude, double longitude) {
    if (latitude < -90 || latitude > 90)
      throw new IllegalArgumentException("Latitude must be between -90 and +90");
    if (longitude < -180 || longitude > 180)
      throw new IllegalArgumentException("Longitude must be between -180 and +180");
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
  }

  protected LatLng(Parcel in) {
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
  }

  public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
    @Override
    public LatLng createFromParcel(Parcel source) {
      return new LatLng(source);
    }

    @Override
    public LatLng[] newArray(int size) {
      return new LatLng[size];
    }
  };
}
