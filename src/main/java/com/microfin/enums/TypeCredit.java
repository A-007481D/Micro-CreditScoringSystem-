package com.microfin.enums;

public enum TypeCredit {
    IMMOBILIER("Crédit Immobilier"),
    CONSOMMATION("Crédit à la Consommation"),
    PERSONNEL("Crédit Personnel"),
    AUTO("Crédit Automobile"),
    EQUIPEMENT("Crédit Équipement"),
    PROFESSIONNEL("Crédit Professionnel");

    private final String description;

    TypeCredit(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
