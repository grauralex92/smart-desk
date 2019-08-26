package com.endava.smartdesk.model;

public enum RegistrationForm {
    GUEST_FORM("Guest Registration Form"),
    SUMMER_PARTY_FORM("Summer Party Registration Form"),
    INNOVATION_LABS_FORM("Innovation Labs Registration Form");

    private String mFormName;

    RegistrationForm(String formName) {
        mFormName = formName;
    }

    public String getFormName() {
        return mFormName;
    }
}
