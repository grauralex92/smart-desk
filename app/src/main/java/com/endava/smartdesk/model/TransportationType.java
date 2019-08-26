package com.endava.smartdesk.model;

public enum TransportationType {
    TRANSPORTATION("Transportation"),
    CAR("Car"),
    BUS("Bus");

    private String mTransportationType;

    TransportationType(String transportationType) {
        mTransportationType = transportationType;
    }

    public String getTransportationType() {
        return mTransportationType;
    }

}
