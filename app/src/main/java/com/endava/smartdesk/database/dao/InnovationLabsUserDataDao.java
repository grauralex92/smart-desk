package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbInnovationLabsUserData;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface InnovationLabsUserDataDao {

    @Insert(onConflict = REPLACE)
    void insert(DbInnovationLabsUserData userData);

    @Query("DELETE FROM DbInnovationLabsUserData")
    void clear();

    @Query("SELECT count(*) FROM DbInnovationLabsUserData")
    int getUsersNumber();

    @Query("SELECT * FROM DbInnovationLabsUserData where email=:email")
    DbInnovationLabsUserData getUserDataByEmail(String email);

    @Query("SELECT * FROM DbInnovationLabsUserData where last_name=:lastName")
    DbInnovationLabsUserData getUserDataByLastName(String lastName);
}
