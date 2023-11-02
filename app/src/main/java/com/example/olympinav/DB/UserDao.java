package com.example.olympinav.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface UserDao {
  @Insert
  void insert(User user);

  @Query("SELECT * FROM users where username = :username AND password = :password")
  User getUserByLoginDetails(String username, String password);

  @Query("SELECT * FROM users WHERE username = :username")
  User checkUsernameIsTaken(String username);

  @Transaction
  @Query("SELECT * FROM users WHERE username = :username")
  UserWithTicketsAndEvents getUserWithTicketsAndEvents(String username);

}
