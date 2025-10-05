package com.microfin.enums;

public enum SecteurActivite {
    AGRICULTURE("Agriculture"),
    SERVICE("Service"),
    COMMERCE("Commerce"),
    CONSTRUCTION("Construction"),
    INDUSTRIE("Industrie"),
    ARTISANAT("Artisanat"),
    TECHNOLOGIE("Technologie"),
    SANTE("Santé"),
    EDUCATION("Éducation"),
    TRANSPORT("Transport");

    private final String description;

    SecteurActivite(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
