package com.example.olympinav.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TicketDao {
  @Insert
  void insert(Ticket ticket);

  @Delete
  void delete(Ticket ticket);
}
