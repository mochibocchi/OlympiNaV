package com.example.olympinav.models;

public enum UsedCapacity {
  LOW("Empty"),
  MEDIUM("Half Full"),
  HIGH("Nearly Full"),
  STANDING("Standing Only"),
  FULL("Full");

  private final String displayString;

  UsedCapacity(String displayString) {
    this.displayString = displayString;
  }

  public String getDisplayString() {
    return displayString;
  }

  public int toProgressBarPercentage() {
    return (int) ((ordinal() + 1) / (double) UsedCapacity.values().length * 100);
  }
}
