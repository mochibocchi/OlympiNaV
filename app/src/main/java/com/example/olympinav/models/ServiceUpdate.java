package com.example.olympinav.models;

import androidx.annotation.DrawableRes;

import java.time.LocalDateTime;

public class ServiceUpdate {
  private String message;
  @DrawableRes private int icon;
  private LocalDateTime lastUpdated;

  public ServiceUpdate(String message, int icon, LocalDateTime lastUpdated) {
    this.message = message;
    this.icon = icon;
    this.lastUpdated = lastUpdated;
  }

  public String getMessage() {
    return message;
  }

  @DrawableRes
  public int getIcon() {
    return icon;
  }

  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }
}
