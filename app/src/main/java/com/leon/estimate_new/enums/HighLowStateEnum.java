package com.leon.estimate_new.enums;

public enum HighLowStateEnum {
    NORMAL(1),
    HIGH(2),
    LOW(3),
    ZERO(4),
    UN_CALCULATED(16);

    private final int value;

    HighLowStateEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
