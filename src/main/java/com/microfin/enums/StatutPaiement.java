package com.microfin.enums;

public enum StatutPaiement {
    PAYE_A_TEMPS("Payé à temps", 0),
    EN_RETARD("En retard", -3),
    PAYE_EN_RETARD("Payé en retard", 3),
    IMPAYE_NON_REGLE("Impayé non réglé", -10),
    IMPAYE_REGLE("Impayé réglé", 5);

    private final String description;
    private final int scoreImpact;

    StatutPaiement(String description, int scoreImpact) {
        this.description = description;
        this.scoreImpact = scoreImpact;
    }

    public String getDescription() {
        return description;
    }

    public int getScoreImpact() {
        return scoreImpact;
    }
}
