package com.example.olympinav.models;

import java.time.LocalDateTime;
import java.util.List;

public class TransportationMethod {
  private LatLng latlng;
  private List<LatLng> route;
  private TransportationMethodType type;
  private LocalDateTime boardAt;
  private LocalDateTime departAt;

  public TransportationMethod(LatLng latlng, List<LatLng> route, TransportationMethodType type, LocalDateTime boardAt, LocalDateTime departAt) {
    this.latlng = latlng;
    this.route = route;
    this.type = type;
    this.boardAt = boardAt;
    this.departAt = departAt;
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

  public TransportationMethodType getType() {
    return type;
  }

  public void setType(TransportationMethodType type) {
    this.type = type;
  }

  public LocalDateTime getBoardAt() {
    return boardAt;
  }

  public void setBoardAt(LocalDateTime boardAt) {
    this.boardAt = boardAt;
  }

  public LocalDateTime getDepartAt() {
    return departAt;
  }

  public void setDepartAt(LocalDateTime departAt) {
    this.departAt = departAt;
  }
}
