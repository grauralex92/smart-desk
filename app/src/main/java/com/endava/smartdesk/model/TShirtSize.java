package com.endava.smartdesk.model;

public enum TShirtSize {
    T_SHIRT_SIZE("T-Shirt Size"),
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL");

    private String mShirtSize;

    TShirtSize(String tShirtSize) {
        mShirtSize = tShirtSize;
    }

    public String getTShirtSize() {
        return mShirtSize;
    }
}
