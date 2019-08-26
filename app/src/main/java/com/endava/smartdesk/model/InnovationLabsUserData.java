package com.endava.smartdesk.model;

public class InnovationLabsUserData extends RegistrationData {

    private String firstName;
    private String lastName;
    private String teamName;
    private String email;
    private String transportation;
    private String tShirtSize;

    public InnovationLabsUserData() {
        // empty constructor
    }

    public InnovationLabsUserData(String firstName, String lastName, String teamName, String email,
                                  String transportation, String tShirtSize) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamName = teamName;
        this.email = email;
        this.transportation = transportation;
        this.tShirtSize = tShirtSize;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getTShirtSize() {
        return tShirtSize;
    }

    public void setTShirtSize(String tShirtSize) {
        this.tShirtSize = tShirtSize;
    }
}
