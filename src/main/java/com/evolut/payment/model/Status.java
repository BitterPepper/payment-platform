package com.evolut.payment.model;

public enum Status {
    NEW,
    COMPLETED,
    CANCELED;

    public static Status fromString(String name) {
        Status result = null;
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(name)) {
                result = status;
                break;
            }
        }
        return result;
    }
}
