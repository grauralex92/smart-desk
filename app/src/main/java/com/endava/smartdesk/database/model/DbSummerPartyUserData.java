package com.endava.smartdesk.database.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.endava.smartdesk.database.utils.DateConverter;

import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class DbSummerPartyUserData {
    @NonNull
    @ColumnInfo(name = "first_name")
    public String firstName;

    @NonNull
    @ColumnInfo(name = "last_name")
    public String lastName;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    public String email;

    @NonNull
    @ColumnInfo(name = "transportation")
    public String transportation;

    @Nullable
    @ColumnInfo(name = "arrival_hour")
    public Date arrivalHour;

    @Nullable
    @ColumnInfo(name = "departure_hour")
    public Date departureHour;
}
