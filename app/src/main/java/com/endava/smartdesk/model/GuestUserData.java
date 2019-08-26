package com.endava.smartdesk.model;

import java.util.Date;

public class GuestUserData extends RegistrationData {

    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private String badgNumber;
    private String purpose;
    private Date arrivalDate;
    private Date departureDate;

    public GuestUserData() {
        // empty constructor
    }

    public GuestUserData(String firstName, String lastName, String companyName, String email,
                         String badge, String purpose, Date arrivalDate, Date departureDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.email = email;
        this.badgNumber = badge;
        this.purpose = purpose;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBadgeNumber() {
        return badgNumber;
    }

    public void setBadgNumber(String badgNumber) {
        this.badgNumber = badgNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
}
