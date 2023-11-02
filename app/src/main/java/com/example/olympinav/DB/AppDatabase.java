package com.example.olympinav.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class, User.class, Ticket.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract UserDao userDao();
    public abstract TicketDao ticketDao();
}