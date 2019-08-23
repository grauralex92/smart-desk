package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbUserData;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface UserDataDao {

    @Insert(onConflict = IGNORE)
    void insert(DbUserData userData);

    @Query("DELETE FROM DbUserData")
    void clear();

    @Query("SELECT count(*) FROM DbUserData")
    int getUserDataSize();

    @Query("SELECT * FROM DbUserData where email=:email")
    DbUserData getUserDataByEmail(String email);

    @Query("SELECT * FROM DbUserData where last_name=:lastName")
    DbUserData getUserDataByLastName(String lastName);

    @Query("SELECT first_name FROM DbUserData")
    List<String> getAllFirstNames();

    @Query("SELECT last_name FROM DbUserData")
    List<String> getAllLastNames();

    @Query("SELECT company_name FROM DbUserData")
    List<String> getAllCompanyNames();
}
