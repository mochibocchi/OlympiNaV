package com.example.olympinav.models;

// Represents how loud a public transport vehicle currently is. In a real application, this enum would be provided to
// us by a server which processed a decibel number.
public enum NoiseLevel {
  LOW("Quiet"),
  MEDIUM("Moderately Loud"),
  HIGH("Very Loud");

  private final String displayString;

  NoiseLevel(String displayString) {
    this.displayString = displayString;
  }

  public String getDisplayString() {
    return displayString;
  }

  // Converts this enum to a value representable by a progress bar.
  public int toProgressBarPercentage() {
    return (int) ((ordinal() + 1) / (double) NoiseLevel.values().length * 100);
  }

}

