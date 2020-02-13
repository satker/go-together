package org.go.together.dto;

public enum HousingType {
    COUCHSERFING("Couchserfing"),
    BOOKING("Booking"),
    LOCAL_HOUSING("Local housing");

    private String description;

    HousingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
