package com.endava.smartdesk.database;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.endava.smartdesk.database.model.DbCompanyMiningData;
import com.endava.smartdesk.database.model.DbFirstNameMiningData;
import com.endava.smartdesk.database.model.DbLastNameMiningData;
import com.endava.smartdesk.database.model.DbRegistrationData;
import com.endava.smartdesk.database.model.DbUserData;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DbInitialization {

    public static void populeDb(@NonNull final SmartDeskDataBase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void populateDbWithMockedData(SmartDeskDataBase db) {
        db.userDataModel().clear();
        db.registrationDataDao().clear();

        DbUserData gabrielUserData = createUserData("Gabriel", "Blaj", "Endava",
                "gabriel.blaj@endava.com", "39", "Employee",
                newDate(2019, 8, 23, 8, 30),
                newDate(2019, 8, 24, 17, 30));
        DbRegistrationData gabrielRegistrationData = createRegistrationData(gabrielUserData.email, "ABC4321");
        insertData(db, gabrielUserData, gabrielRegistrationData);
        DbUserData alexUserData = createUserData("Alex", "Graur", "Endava",
                "alex.graur@endava.com", "61", "Employee",
                newDate(2019, 8, 25, 9, 0),
                newDate(2019, 8, 26, 18, 0));
        DbRegistrationData alexRegistrationData = createRegistrationData(alexUserData.email, "QWERTY1");
        insertData(db, alexUserData, alexRegistrationData);
        DbUserData mariusUserData = createUserData("Marius", "Olenici", "Endava",
                "marius.olenici@endava.com", "13", "Employee",
                newDate(2019, 8, 27, 10, 15),
                newDate(2019, 8, 28, 18, 30));
        DbRegistrationData mariusRegistrationData = createRegistrationData(mariusUserData.email, "1234ABC");
        insertData(db, mariusUserData, mariusRegistrationData);
        DbUserData mihaiUserData = createUserData("Mihai", "Rosu", "Endava",
                "mihai.rosu@endava.com", "23", "Employee",
                newDate(2019, 8, 26, 10, 30),
                newDate(2019, 8, 27, 19, 0));
        DbRegistrationData mihaiRegistrationData = createRegistrationData(mihaiUserData.email, "ASDFG12");
        insertData(db, mihaiUserData, mihaiRegistrationData);

        insertMiningData(db);
    }

    private static void insertMiningData(SmartDeskDataBase db) {
        String[] lastNames = {"Popescu", "Ionescu", "Popa", "Niță", "Nițu", "Constantinescu", "Stan", "Stanciu"
                , "Dumitrescu", "Dima", "Gheorghiu", "Ioniță", "Marin", "Tudor", "Dobre", "Barbu", "Nistor"
                , "Florea", "Frățilă", "Dinu", "Dinescu", "Georgescu", "Stoica", "Diaconu", "Diaconescu"
                , "Mocanu", "Voinea", "Albu", "Petrescu", "Manole", "Cristea", "Toma", "Stănescu", "Pușcașu"
                , "Blaj", "Tomescu", "Sava", "Ciobanu", "Rusu", "Ursu", "Lupu", "Munteanu", "Mehedințu"
                , "Andreescu", "Sava", "Mihăilescu", "Iancu", "Blaga", "Teodoru", "Teodorescu", "Olenici"
                , "Moise", "Moisescu", "Călinescu", "Tabacu", "Negoiță", "Graur", "Rosu", "Blaj"};
        for (int i = 0; i < lastNames.length; i++) {
            insertLastNameData(db, lastNames[i]);
        }

        String[] firstNames = {"Gabriel", "Alex", "Alexandru", "Mihai", "Marius", "Dan", "Robert"
                , "Andrei", "Catalin", "Oana", "Daniela", "Carmen", "Simina", "Cătălina", "Anca"
                , "Carmen", "Dana", "Delia", "Silviu", "Daiana", "Diana", "Alexandra", "Ioana"
                , "Larisa", "Paula", "Nicoleta", "Betina", "Beatrice", "Doris", "Corina", "Alina"
                , "Florin", "Calin", "Albert", "Codin", "Costin", "Costel", "Viorel", "Edward", "Lucian"
                , "Marcel", "Manole", "Marian"};
        for (int i = 0; i < firstNames.length; i++) {
            insertFirstNameData(db, firstNames[i]);
        }

        String[] companyNames = {"Endava", "Telekom", "Orange", "Vodafone", "Yardi", "Yonder",
                "Microsoft", "Facebook", "Google", "Telenav", "Accenture", "Endava"};
        for (int i = 0; i < companyNames.length; i++) {
            insertCompanyData(db, companyNames[i]);
        }
    }

    private static void insertFirstNameData(SmartDeskDataBase db, String firstName) {
        DbFirstNameMiningData firstNameMiningData = new DbFirstNameMiningData();
        firstNameMiningData.firstName = firstName;
        db.firstNameMiningDao().insert(firstNameMiningData);
    }

    private static void insertLastNameData(SmartDeskDataBase db, String lastName) {
        DbLastNameMiningData lastNameMiningData = new DbLastNameMiningData();
        lastNameMiningData.lastName = lastName;
        db.lastNameMiningDao().insert(lastNameMiningData);
    }

    private static void insertCompanyData(SmartDeskDataBase db, String companyName) {
        DbCompanyMiningData companyMiningData = new DbCompanyMiningData();
        companyMiningData.company = companyName;
        db.companyMiningDao().insert(companyMiningData);
    }

    private static DbUserData createUserData(String firstName, String lastName,
                                             String companyName, String email, String badgeNumber,
                                             String purpose, Date arrivalDate, Date departureDate) {
        DbUserData userData = new DbUserData();
        userData.firstName = firstName;
        userData.lastName = lastName;
        userData.companyName = companyName;
        userData.email = email;
        userData.badgeNumber = badgeNumber;
        userData.purpose = purpose;
        userData.arrivalDate = arrivalDate;
        userData.departureDate = departureDate;
        return userData;
    }

    private static DbRegistrationData createRegistrationData(String email, String registrationCode) {
        DbRegistrationData registrationData = new DbRegistrationData();
        registrationData.email = email;
        registrationData.registrationCode = registrationCode;
        return registrationData;
    }

    private static void insertData(SmartDeskDataBase db, DbUserData userData,
                                   DbRegistrationData registrationData) {
        db.userDataModel().insert(userData);
        db.registrationDataDao().insert(registrationData);
    }

    private static Date newDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = GregorianCalendar.getInstance(Locale.US);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final SmartDeskDataBase mDb;

        PopulateDbAsync(SmartDeskDataBase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateDbWithMockedData(mDb);
            return null;
        }
    }
}
