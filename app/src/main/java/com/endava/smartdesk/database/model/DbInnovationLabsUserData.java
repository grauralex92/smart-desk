package com.endava.smartdesk.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DbInnovationLabsUserData {
    @NonNull
    @ColumnInfo(name = "first_name")
    public String firstName;

    @NonNull
    @ColumnInfo(name = "last_name")
    public String lastName;

    @NonNull
    @ColumnInfo(name = "team_name")
    public String teamName;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    public String email;

    @NonNull
    @ColumnInfo(name = "transportation")
    public String transportation;

    @NonNull
    @ColumnInfo(name = "t_shirt_size")
    public String tShirtSize;
}
