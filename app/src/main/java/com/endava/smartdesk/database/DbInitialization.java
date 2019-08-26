package com.endava.smartdesk.database;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.endava.smartdesk.database.model.DbCompanyMiningData;
import com.endava.smartdesk.database.model.DbFirstNameMiningData;
import com.endava.smartdesk.database.model.DbGuestUserData;
import com.endava.smartdesk.database.model.DbInnovationLabsUserData;
import com.endava.smartdesk.database.model.DbLastNameMiningData;
import com.endava.smartdesk.database.model.DbRegistrationData;
import com.endava.smartdesk.database.model.DbSummerPartyUserData;
import com.endava.smartdesk.model.RegistrationForm;

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
        db.guestUserDataModel().clear();
        db.innovationLabsUserDataModel().clear();
        db.summerPartyUserDataModel().clear();
        db.registrationDataDao().clear();

        DbGuestUserData gabrielGuestUserData = createGuestUserData("Gabriel", "Blaj", "Endava",
                "gabriel.blaj@endava.com", "39", "Employee",
                newDateAndHour(2019, 8, 23, 8, 30),
                newDateAndHour(2019, 8, 24, 17, 30));
        DbRegistrationData gabrielGuestRegistrationData = createRegistrationData(gabrielGuestUserData.email,
                "ABC4321", RegistrationForm.GUEST_FORM.getFormName());
        insertGuestData(db, gabrielGuestUserData, gabrielGuestRegistrationData);

        DbSummerPartyUserData gabrielSummerPartyUserData = createSummerPartyUserData("Gabriel", "Blaj",
                "gabriel.blaj@endava.com", "Bus", newHour(17, 0), newHour(3, 0));
        DbRegistrationData gabrielSummerPartyRegistrationData = createRegistrationData(gabrielSummerPartyUserData.email,
                "SUMMER12", RegistrationForm.SUMMER_PARTY_FORM.getFormName());
        insertSummerPartyData(db, gabrielSummerPartyUserData, gabrielSummerPartyRegistrationData);

        DbInnovationLabsUserData gabrielInnovationLabsUserData = createInnovationLabsUserData("Gabriel", "Blaj",
                "SSI", "gabriel.blaj@endava.com", "Car", "M");
        DbRegistrationData gabrielInnovationLabsRegistrationData = createRegistrationData(gabrielInnovationLabsUserData.email,
                "INN1234", RegistrationForm.INNOVATION_LABS_FORM.getFormName());
        insertInnovationLabsData(db, gabrielInnovationLabsUserData, gabrielInnovationLabsRegistrationData);

        DbGuestUserData alexGuestUserData = createGuestUserData("Alex", "Graur", "Endava",
                "alex.graur@endava.com", "61", "Employee",
                newDateAndHour(2019, 8, 25, 9, 0),
                newDateAndHour(2019, 8, 26, 18, 0));
        DbRegistrationData alexGuestRegistrationData = createRegistrationData(alexGuestUserData.email,
                "QWERTY1", RegistrationForm.GUEST_FORM.getFormName());
        insertGuestData(db, alexGuestUserData, alexGuestRegistrationData);

        DbSummerPartyUserData alexSummerPartyUserData = createSummerPartyUserData("Alex", "Graur",
                "alex.graur@endava.com", "Car", newHour(15, 0), newHour(1, 0));
        DbRegistrationData alexSummerPartyRegistrationData = createRegistrationData(alexSummerPartyUserData.email,
                "SUMMER32", RegistrationForm.SUMMER_PARTY_FORM.getFormName());
        insertSummerPartyData(db, alexSummerPartyUserData, alexSummerPartyRegistrationData);

        DbGuestUserData mariusUserData = createGuestUserData("Marius", "Olenici", "Endava",
                "marius.olenici@endava.com", "13", "Employee",
                newDateAndHour(2019, 8, 27, 10, 15),
                newDateAndHour(2019, 8, 28, 18, 30));
        DbRegistrationData mariusRegistrationData = createRegistrationData(mariusUserData.email,
                "1234ABC", RegistrationForm.GUEST_FORM.getFormName());
        insertGuestData(db, mariusUserData, mariusRegistrationData);

        DbInnovationLabsUserData mariusInnovationLabsUserData = createInnovationLabsUserData("Marius", "Olenici",
                "SSI", "marius.olenici@endava.com", "Car", "M");
        DbRegistrationData mariusInnovationLabsRegistrationData = createRegistrationData(mariusInnovationLabsUserData.email,
                "INN4321", RegistrationForm.INNOVATION_LABS_FORM.getFormName());
        insertInnovationLabsData(db, mariusInnovationLabsUserData, mariusInnovationLabsRegistrationData);

        DbGuestUserData mihaiGuestUserData = createGuestUserData("Mihai", "Rosu", "Endava",
                "mihai.rosu@endava.com", "23", "Employee",
                newDateAndHour(2019, 8, 26, 10, 30),
                newDateAndHour(2019, 8, 27, 19, 0));
        DbRegistrationData mihaiGuestRegistrationData = createRegistrationData(mihaiGuestUserData.email,
                "ASDFG12", RegistrationForm.GUEST_FORM.getFormName());
        insertGuestData(db, mihaiGuestUserData, mihaiGuestRegistrationData);

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

    private static DbSummerPartyUserData createSummerPartyUserData(String firstName, String lastName, String email,
                                                                   String transportation, Date arrivalHour, Date departureHour) {
        DbSummerPartyUserData userData = new DbSummerPartyUserData();
        userData.firstName = firstName;
        userData.lastName = lastName;
        userData.email = email;
        userData.transportation = transportation;
        userData.arrivalHour = arrivalHour;
        userData.departureHour = departureHour;
        return userData;
    }

    private static DbInnovationLabsUserData createInnovationLabsUserData(String firstName, String lastName, String teamName,
                                                                         String email, String transportation, String tShirtSize) {
        DbInnovationLabsUserData userData = new DbInnovationLabsUserData();
        userData.firstName = firstName;
        userData.lastName = lastName;
        userData.teamName = teamName;
        userData.email = email;
        userData.transportation = transportation;
        userData.tShirtSize = tShirtSize;
        return userData;
    }

    private static DbGuestUserData createGuestUserData(String firstName, String lastName,
                                                       String companyName, String email, String badgeNumber,
                                                       String purpose, Date arrivalDate, Date departureDate) {
        DbGuestUserData userData = new DbGuestUserData();
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

    private static DbRegistrationData createRegistrationData(String email, String registrationCode,
                                                             String formType) {
        DbRegistrationData registrationData = new DbRegistrationData();
        registrationData.email = email;
        registrationData.registrationCode = registrationCode;
        registrationData.formType = formType;
        return registrationData;
    }

    private static void insertGuestData(SmartDeskDataBase db, DbGuestUserData userData,
                                        DbRegistrationData registrationData) {
        db.guestUserDataModel().insert(userData);
        db.registrationDataDao().insert(registrationData);
    }

    private static void insertSummerPartyData(SmartDeskDataBase db, DbSummerPartyUserData userData,
                                              DbRegistrationData registrationData) {
        db.summerPartyUserDataModel().insert(userData);
        db.registrationDataDao().insert(registrationData);
    }

    private static void insertInnovationLabsData(SmartDeskDataBase db, DbInnovationLabsUserData userData,
                                                 DbRegistrationData registrationData) {
        db.innovationLabsUserDataModel().insert(userData);
        db.registrationDataDao().insert(registrationData);
    }

    private static Date newHour(int hour, int minute) {
        Calendar calendar = GregorianCalendar.getInstance(Locale.US);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private static Date newDateAndHour(int year, int month, int day, int hour, int minute) {
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
