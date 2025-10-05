package com.microfin.enums;

public enum TypeContrat {
    CDI("CDI"),
    CDD("CDD"),
    INTERIM("Intérim"),
    PROFESSION_LIBERALE("Profession libérale"),
    AUTO_ENTREPRENEUR("Auto-entrepreneur");

    private final String description;

    TypeContrat(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
