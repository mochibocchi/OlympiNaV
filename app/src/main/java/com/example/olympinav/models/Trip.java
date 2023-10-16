package com.example.olympinav.models;

import java.time.LocalDateTime;
import java.util.List;

public class Trip {
  private LatLng startLocation;
  private LatLng endLocation;
  private LocalDateTime leaveAt;
  private LocalDateTime arriveAt;
  private List<TransportationMethod> iDontKnow;

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
}
