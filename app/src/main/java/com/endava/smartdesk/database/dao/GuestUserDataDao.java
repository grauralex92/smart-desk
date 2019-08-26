package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbGuestUserData;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface GuestUserDataDao {

    @Insert(onConflict = REPLACE)
    void insert(DbGuestUserData userData);

    @Query("DELETE FROM DbGuestUserData")
    void clear();

    @Query("SELECT count(*) FROM DbGuestUserData")
    int getUsersNumber();

    @Query("SELECT * FROM DbGuestUserData where email=:email")
    DbGuestUserData getUserDataByEmail(String email);

    @Query("SELECT * FROM DbGuestUserData where last_name=:lastName")
    DbGuestUserData getUserDataByLastName(String lastName);
}
