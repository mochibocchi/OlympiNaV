package com.example.olympinav.DB;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TicketWithEvent {
  @Embedded
  private Ticket ticket;

  @Relation(parentColumn = "eventId", entityColumn = "id", entity = Event.class)
  private Event event;

  public TicketWithEvent(Ticket ticket, Event event) {
    this.ticket = ticket;
    this.event = event;
  }

  public Ticket getTicket() {
    return ticket;
  }

  public Event getEvent() {
    return event;
  }

}
