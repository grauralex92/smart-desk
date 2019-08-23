package com.endava.smartdesk.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "company", unique = true)})
public class DbCompanyMiningData {
    @PrimaryKey
    @NonNull
    public String company;
}