package com.example.olympinav.models;

public enum UsedCapacity {
  LOW("Empty"),
  MEDIUM("Half Full"),
  HIGH("Nearly Full"),
  STANDING("Standing Room Only"),
  FULL("Full");

  private final String displayString;

  UsedCapacity(String displayString) {
    this.displayString = displayString;
  }

  public String getDisplayString() {
    return displayString;
  }

  public int toProgressBarPercentage() {
    return ordinal() + 1 / UsedCapacity.values().length * 100;
  }
}
