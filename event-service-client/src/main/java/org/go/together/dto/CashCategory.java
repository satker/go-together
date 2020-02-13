package org.go.together.dto;

public enum CashCategory {
    FIFTY_FIFTY("50 / 50"),
    FOR_FREE("Free"),
    PAYED_YOURSELF("Yourself");

    private String description;

    CashCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
