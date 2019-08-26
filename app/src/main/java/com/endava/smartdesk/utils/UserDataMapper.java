package com.endava.smartdesk.utils;

import com.endava.smartdesk.database.model.DbGuestUserData;
import com.endava.smartdesk.database.model.DbInnovationLabsUserData;
import com.endava.smartdesk.database.model.DbSummerPartyUserData;
import com.endava.smartdesk.model.GuestUserData;
import com.endava.smartdesk.model.InnovationLabsUserData;
import com.endava.smartdesk.model.SummerPartyUserData;

public class UserDataMapper {

    public UserDataMapper() {
    }

    public GuestUserData convertToGuestUserData(DbGuestUserData dbGuestUserData) {
        if (dbGuestUserData == null) {
            return null;
        }
        GuestUserData guestUserData = new GuestUserData();
        guestUserData.setFirstName(dbGuestUserData.firstName);
        guestUserData.setLastName(dbGuestUserData.lastName);
        guestUserData.setCompanyName(dbGuestUserData.companyName);
        guestUserData.setEmail(dbGuestUserData.email);
        guestUserData.setBadgNumber(dbGuestUserData.badgeNumber);
        guestUserData.setPurpose(dbGuestUserData.purpose);
        guestUserData.setArrivalDate(dbGuestUserData.arrivalDate);
        guestUserData.setDepartureDate(dbGuestUserData.departureDate);
        return guestUserData;
    }

    public DbGuestUserData convertToDbGuestUserData(GuestUserData guestUserData) {
        if (guestUserData == null) {
            return null;
        }
        DbGuestUserData dbGuestUserData = new DbGuestUserData();
        dbGuestUserData.firstName = guestUserData.getFirstName();
        dbGuestUserData.lastName = guestUserData.getLastName();
        dbGuestUserData.companyName = guestUserData.getCompanyName();
        dbGuestUserData.email = guestUserData.getEmail();
        dbGuestUserData.badgeNumber = guestUserData.getBadgeNumber();
        dbGuestUserData.purpose = guestUserData.getPurpose();
        dbGuestUserData.arrivalDate = guestUserData.getArrivalDate();
        dbGuestUserData.departureDate = guestUserData.getDepartureDate();
        return dbGuestUserData;
    }

    public SummerPartyUserData convertToSummerPartyUserData(DbSummerPartyUserData dbSummerPartyUserData) {
        if (dbSummerPartyUserData == null) {
            return null;
        }
        SummerPartyUserData summerPartyUserData = new SummerPartyUserData();
        summerPartyUserData.setFirstName(dbSummerPartyUserData.firstName);
        summerPartyUserData.setLastName(dbSummerPartyUserData.lastName);
        summerPartyUserData.setEmail(dbSummerPartyUserData.email);
        summerPartyUserData.setTransportation(dbSummerPartyUserData.transportation);
        summerPartyUserData.setArrivalHour(dbSummerPartyUserData.arrivalHour);
        summerPartyUserData.setDepartureHour(dbSummerPartyUserData.departureHour);
        return summerPartyUserData;
    }

    public DbSummerPartyUserData convertToDbSummerPartyUserData(SummerPartyUserData summerPartyUserData) {
        if (summerPartyUserData == null) {
            return null;
        }
        DbSummerPartyUserData dbSummerPartyUserData = new DbSummerPartyUserData();
        dbSummerPartyUserData.firstName = summerPartyUserData.getFirstName();
        dbSummerPartyUserData.lastName = summerPartyUserData.getLastName();
        dbSummerPartyUserData.email = summerPartyUserData.getEmail();
        dbSummerPartyUserData.transportation = summerPartyUserData.getTransportation();
        dbSummerPartyUserData.arrivalHour = summerPartyUserData.getArrivalHour();
        dbSummerPartyUserData.departureHour = summerPartyUserData.getDepartureHour();
        return dbSummerPartyUserData;
    }

    public InnovationLabsUserData convertToInnovationLabsUserData(DbInnovationLabsUserData dbInnovationLabsUserData) {
        if (dbInnovationLabsUserData == null) {
            return null;
        }
        InnovationLabsUserData innovationLabsUserData = new InnovationLabsUserData();
        innovationLabsUserData.setFirstName(dbInnovationLabsUserData.firstName);
        innovationLabsUserData.setLastName(dbInnovationLabsUserData.lastName);
        innovationLabsUserData.setTeamName(dbInnovationLabsUserData.teamName);
        innovationLabsUserData.setEmail(dbInnovationLabsUserData.email);
        innovationLabsUserData.setTransportation(dbInnovationLabsUserData.transportation);
        innovationLabsUserData.setTShirtSize(dbInnovationLabsUserData.tShirtSize);
        return innovationLabsUserData;
    }

    public DbInnovationLabsUserData convertToDbInnovationLabsUserData(InnovationLabsUserData innovationLabsUserData) {
        if (innovationLabsUserData == null) {
            return null;
        }
        DbInnovationLabsUserData dbInnovationLabsUserData = new DbInnovationLabsUserData();
        dbInnovationLabsUserData.firstName = innovationLabsUserData.getFirstName();
        dbInnovationLabsUserData.lastName = innovationLabsUserData.getLastName();
        dbInnovationLabsUserData.teamName = innovationLabsUserData.getTeamName();
        dbInnovationLabsUserData.email = innovationLabsUserData.getEmail();
        dbInnovationLabsUserData.transportation = innovationLabsUserData.getTransportation();
        dbInnovationLabsUserData.tShirtSize = innovationLabsUserData.getTShirtSize();
        return dbInnovationLabsUserData;
    }
}
