package com.microfin.enums;

public enum DecisionCredit {
    ACCORD_IMMEDIAT("Accord immédiat"),
    ETUDE_MANUELLE("Étude manuelle requise"),
    REFUS_AUTOMATIQUE("Refus automatique");

    private final String description;

    DecisionCredit(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
