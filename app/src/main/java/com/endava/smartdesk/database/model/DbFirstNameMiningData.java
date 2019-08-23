package com.endava.smartdesk.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "first_name", unique = true)})
public class DbFirstNameMiningData {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "first_name")
    public String firstName;
}