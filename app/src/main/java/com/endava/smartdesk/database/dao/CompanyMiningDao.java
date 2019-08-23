package com.endava.smartdesk.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.endava.smartdesk.database.model.DbCompanyMiningData;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface CompanyMiningDao {

    @Insert(onConflict = IGNORE)
    void insert(DbCompanyMiningData companyMiningData);

    @Query("SELECT count(*) FROM DbCompanyMiningData")
    int getCompanyMiningDataSize();

    @Query("DELETE FROM DbCompanyMiningData")
    void clear();

    @Query("SELECT company FROM DbCompanyMiningData")
    List<String> getAllCompanyNames();
}