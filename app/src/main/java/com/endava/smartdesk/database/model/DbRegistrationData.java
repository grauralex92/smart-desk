package com.endava.smartdesk.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DbRegistrationData {
    @NonNull
    @ColumnInfo(name = "email")
    public String email;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "registration_code")
    public String registrationCode;

    @NonNull
    @ColumnInfo(name = "form_type")
    public String formType;
}
