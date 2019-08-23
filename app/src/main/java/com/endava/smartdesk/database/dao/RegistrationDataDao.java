package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbRegistrationData;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface RegistrationDataDao {

    @Insert(onConflict = IGNORE)
    void insert(DbRegistrationData registrationData);

    @Query("SELECT count(*) FROM DbRegistrationData")
    int getRegistrationDataSize();

    @Query("DELETE FROM DbRegistrationData")
    void clear();

    @Query("SELECT * FROM DbRegistrationData")
    List<DbRegistrationData> getAllRegistrationData();

    @Query("SELECT * FROM DbRegistrationData where registration_code=:registrationCode")
    DbRegistrationData getRegistrationData(String registrationCode);
}
