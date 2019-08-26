package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbSummerPartyUserData;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SummerPartyUserDataDao {

    @Insert(onConflict = REPLACE)
    void insert(DbSummerPartyUserData userData);

    @Query("DELETE FROM DbSummerPartyUserData")
    void clear();

    @Query("SELECT count(*) FROM DbSummerPartyUserData")
    int getUsersNumber();

    @Query("SELECT * FROM DbSummerPartyUserData where email=:email")
    DbSummerPartyUserData getUserDataByEmail(String email);

    @Query("SELECT * FROM DbSummerPartyUserData where last_name=:lastName")
    DbSummerPartyUserData getUserDataByLastName(String lastName);
}
