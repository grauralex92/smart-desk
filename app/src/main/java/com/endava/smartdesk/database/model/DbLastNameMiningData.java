package com.endava.smartdesk.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "last_name", unique = true)})
public class DbLastNameMiningData {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "last_name")
    public String lastName;
}