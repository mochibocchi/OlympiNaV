package com.example.olympinav.DB;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TicketDao {
  @Insert
  void insert(Ticket ticket);
}
