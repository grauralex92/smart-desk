package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbFirstNameMiningData;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface FirstNameMiningDao {

    @Insert(onConflict = IGNORE)
    void insert(DbFirstNameMiningData firstNameMiningData);

    @Query("SELECT count(*) FROM DbFirstNameMiningData")
    int getFirstNameMiningDataSize();

    @Query("DELETE FROM DbFirstNameMiningData")
    void clear();

    @Query("SELECT first_name FROM DbFirstNameMiningData")
    List<String> getAllFirstNames();
}