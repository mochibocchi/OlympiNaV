package com.example.olympinav.models;

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

  public int toProgressBarPercentage() {
    return ordinal() + 1 / UsedCapacity.values().length * 100;
  }

}

