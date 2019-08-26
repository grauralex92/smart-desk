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
public class DbGuestUserData {
    @NonNull
    @ColumnInfo(name = "first_name")
    public String firstName;

    @NonNull
    @ColumnInfo(name = "last_name")
    public String lastName;

    @NonNull
    @ColumnInfo(name = "company_name")
    public String companyName;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    public String email;

    @NonNull
    @ColumnInfo(name = "badge_number")
    public String badgeNumber;

    @NonNull
    @ColumnInfo(name = "purpose")
    public String purpose;

    @Nullable
    @ColumnInfo(name = "arrival_date")
    public Date arrivalDate;

    @Nullable
    @ColumnInfo(name = "departure_date")
    public Date departureDate;
}
