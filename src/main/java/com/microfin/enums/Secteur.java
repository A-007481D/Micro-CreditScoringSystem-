package com.microfin.enums;

public enum Secteur {
    PUBLIC("Secteur Public", 25),
    GRANDE_ENTREPRISE("Grande Entreprise Privée", 15),
    PME("PME Privée", 12);

    private final String description;
    private final int scoreStabilite;

    Secteur(String description, int scoreStabilite) {
        this.description = description;
        this.scoreStabilite = scoreStabilite;
    }

    public String getDescription() {
        return description;
    }

    public int getScoreStabilite() {
        return scoreStabilite;
    }
}
