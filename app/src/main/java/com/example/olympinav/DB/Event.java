package com.example.olympinav.DB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String ticketId;
    private String eventName;
    private String date;
    private Integer imageId;

    public Event(String ticketId, String eventName, String date, Integer imageId) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.date = date;
        this.imageId = imageId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public Integer getImageId() { return imageId; }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
