package com.microfin.enums;

public enum SituationFamiliale {
    MARIE("Marié", 3),
    CELIBATAIRE("Célibataire", 2),
    DIVORCE("Divorcé", 2),
    VEUF("Veuf", 2);

    private final String description;
    private final int scoreRelation;

    SituationFamiliale(String description, int scoreRelation) {
        this.description = description;
        this.scoreRelation = scoreRelation;
    }

    public String getDescription() {
        return description;
    }

    public int getScoreRelation() {
        return scoreRelation;
    }
}
