package com.example.olympinav.DB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithTicketsAndEvents {
  @Embedded
  private User user;

  @Relation(parentColumn = "username", entityColumn = "ownerUsername", entity = Ticket.class)
  private List<TicketWithEvent> tickets;

  public UserWithTicketsAndEvents(User user, List<TicketWithEvent> tickets) {
    this.user = user;
    this.tickets = tickets;
  }

  public User getUser() {
    return user;
  }

  public List<TicketWithEvent> getTickets() {
    return tickets;
  }
}
