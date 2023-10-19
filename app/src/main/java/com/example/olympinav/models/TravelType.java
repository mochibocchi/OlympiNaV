package com.example.olympinav.models;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.example.olympinav.R;

public enum TravelType {
  BUS(R.color.bus_color, R.drawable.baseline_directions_bus_24),
  TRAIN(R.color.train_color, R.drawable.baseline_train_24),
  FERRY(R.color.ferry_color, R.drawable.baseline_ferry_24),
  WALK(R.color.walk_color, R.drawable.baseline_directions_walk_24);

  @ColorRes private final int color;
  @DrawableRes private final int drawable;

  TravelType(@ColorRes int color, @DrawableRes int drawable) {
    this.color = color;
    this.drawable = drawable;
  }

  public int getColor() {
    return color;
  }

  public int getDrawable() {
    return drawable;
  }
}
