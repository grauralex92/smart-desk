package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbLastNameMiningData;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface LastNameMiningDao {

    @Insert(onConflict = IGNORE)
    void insert(DbLastNameMiningData lastNameMiningData);

    @Query("SELECT count(*) FROM DbLastNameMiningData")
    int getLastNameMiningDataSize();

    @Query("DELETE FROM DbLastNameMiningData")
    void clear();

    @Query("SELECT last_name FROM DbLastNameMiningData")
    List<String> getAllLastNames();
}