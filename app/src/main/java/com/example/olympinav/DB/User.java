package com.example.olympinav.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "users")
public class User {
  @PrimaryKey
  @NonNull
  private String username;
  private String password;
  private Integer noiseBaselineLevel;

  public User(String username, String password, Integer noiseBaselineLevel) {
    this.username = username;
    this.password = password;
    this.noiseBaselineLevel = noiseBaselineLevel;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Integer getNoiseBaselineLevel() {
    return noiseBaselineLevel;
  }
}
