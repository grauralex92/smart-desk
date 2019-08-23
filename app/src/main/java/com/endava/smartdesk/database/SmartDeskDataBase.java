package com.endava.smartdesk.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.endava.smartdesk.database.dao.CompanyMiningDao;
import com.endava.smartdesk.database.dao.FirstNameMiningDao;
import com.endava.smartdesk.database.dao.LastNameMiningDao;
import com.endava.smartdesk.database.dao.RegistrationDataDao;
import com.endava.smartdesk.database.dao.UserDataDao;
import com.endava.smartdesk.database.model.DbCompanyMiningData;
import com.endava.smartdesk.database.model.DbFirstNameMiningData;
import com.endava.smartdesk.database.model.DbLastNameMiningData;
import com.endava.smartdesk.database.model.DbRegistrationData;
import com.endava.smartdesk.database.model.DbUserData;

@Database(entities = {DbUserData.class, DbRegistrationData.class, DbFirstNameMiningData.class
        , DbLastNameMiningData.class, DbCompanyMiningData.class}, version = 1)
public abstract class SmartDeskDataBase extends RoomDatabase {

    private static final String DB_NAME = "SmartDeskDB.db";
    private static SmartDeskDataBase sInstance;

    public abstract UserDataDao userDataModel();

    public abstract RegistrationDataDao registrationDataDao();

    public abstract FirstNameMiningDao firstNameMiningDao();

    public abstract LastNameMiningDao lastNameMiningDao();

    public abstract CompanyMiningDao companyMiningDao();

    public static SmartDeskDataBase getDatabase(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), SmartDeskDataBase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public static void clearInstance() {
        sInstance = null;
    }

}
