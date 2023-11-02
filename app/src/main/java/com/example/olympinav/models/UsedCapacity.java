package com.example.olympinav.models;

// Represents how full a public transport vehicle currently is. In a real application, this enum would be provided to
// us by a server which processed data received by a camera.
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

  // Converts this enum to a value representable by a progress bar.
  public int toProgressBarPercentage() {
    return (int) ((ordinal() + 1) / (double) UsedCapacity.values().length * 100);
  }
}
