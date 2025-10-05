package com.microfin.enums;

public enum TypeIncident {
    PAYE_A_TEMPS("Payé à temps", 0),
    EN_RETARD("En retard", -3),
    PAYE_EN_RETARD("Payé en retard", 3),
    IMPAYE_NON_REGLE("Impayé non réglé", -10),
    IMPAYE_REGLE("Impayé réglé", 5);

    private final String description;
    private final int penalite;

    TypeIncident(String description, int penalite) {
        this.description = description;
        this.penalite = penalite;
    }

    public String getDescription() {
        return description;
    }

    public int getPenalite() {
        return penalite;
    }
}
