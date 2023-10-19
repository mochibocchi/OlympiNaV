package com.example.olympinav.models;

public enum UsedCapacity {
  FULL("Full"),
  STANDING("Standing Room Only"),
  HIGH("Nearly Full"),
  MEDIUM("Half Full"),
  LOW("Empty");

  private final String displayString;

  UsedCapacity(String displayString) {
    this.displayString = displayString;
  }

  public String getDisplayString() {
    return displayString;
  }
}
