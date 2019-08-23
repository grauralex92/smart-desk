package com.endava.smartdesk.utils;

import com.endava.smartdesk.database.model.DbUserData;
import com.endava.smartdesk.model.UserData;

public class UserDataMapper {

    public UserDataMapper() {
    }

    public DbUserData converToDbUserData(UserData userData) {
        if (userData == null) {
            return null;
        }
        DbUserData dbUserData = new DbUserData();
        dbUserData.firstName = userData.getFirstName();
        dbUserData.lastName = userData.getLastName();
        dbUserData.companyName = userData.getCompanyName();
        dbUserData.email = userData.getEmail();
        dbUserData.badgeNumber = userData.getBadgeNumber();
        dbUserData.purpose = userData.getPurpose();
        dbUserData.arrivalDate = userData.getArrivalDate();
        dbUserData.departureDate = userData.getDepartureDate();
        return dbUserData;
    }

    public UserData convertToUserData(DbUserData dbUserData) {
        if (dbUserData == null) {
            return null;
        }
        UserData userData = new UserData();
        userData.setFirstName(dbUserData.firstName);
        userData.setLastName(dbUserData.lastName);
        userData.setCompanyName(dbUserData.companyName);
        userData.setEmail(dbUserData.email);
        userData.setBadgNumber(dbUserData.badgeNumber);
        userData.setPurpose(dbUserData.purpose);
        userData.setArrivalDate(dbUserData.arrivalDate);
        userData.setDepartureDate(dbUserData.departureDate);
        return userData;
    }

}
