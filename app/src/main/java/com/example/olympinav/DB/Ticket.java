package com.example.olympinav.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tickets")
public class Ticket {
  @PrimaryKey(autoGenerate = true)
  private Long id;
  private Long eventId;
  private String ownerUsername;

  public Ticket(Long eventId, String ownerUsername) {
    this.eventId = eventId;
    this.ownerUsername = ownerUsername;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEventId() {
    return eventId;
  }

  public String getOwnerUsername() {
    return ownerUsername;
  }
}
