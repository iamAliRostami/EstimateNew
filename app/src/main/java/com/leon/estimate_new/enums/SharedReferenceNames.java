package com.leon.estimate_new.enums;

public enum SharedReferenceNames {
    ACCOUNT("com.app.leon.estimate_new.account_info");

    private final String value;

    SharedReferenceNames(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
