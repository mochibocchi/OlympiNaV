package com.example.olympinav.models;

public enum NoiseLevel {
  HIGH("Very Loud"),
  MEDIUM("Moderately Loud"),
  LOW("Quiet");

  private final String displayString;

  NoiseLevel(String displayString) {
    this.displayString = displayString;
  }

  public String getDisplayString() {
    return displayString;
  }
}

