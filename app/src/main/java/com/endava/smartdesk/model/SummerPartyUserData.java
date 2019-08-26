package com.endava.smartdesk.model;

import java.util.Date;

public class SummerPartyUserData extends RegistrationData {

    private String firstName;
    private String lastName;
    private String email;
    private String transportation;
    private Date arrivalHour;
    private Date departureHour;

    public SummerPartyUserData() {
        // empty constructor
    }

    public SummerPartyUserData(String firstName, String lastName, String companyName, String email,
                               String badge, String purpose, Date arrivalHour, Date departureHour) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.transportation = badge;
        this.arrivalHour = arrivalHour;
        this.departureHour = departureHour;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public Date getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(Date arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public Date getDepartureHour() {
        return departureHour;
    }

    public void setDepartureHour(Date departureHour) {
        this.departureHour = departureHour;
    }
}
