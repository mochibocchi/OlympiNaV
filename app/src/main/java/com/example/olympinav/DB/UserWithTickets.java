package com.example.olympinav.DB;

import java.util.List;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

public class UserWithTickets {
  @Embedded private User user;
  @Relation(parentColumn = "username", entityColumn = "ownerUsername")
  private List<TicketWithEvent> tickets;
}
