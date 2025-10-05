package com.microfin.model;

import com.microfin.enums.TypeIncident;
import java.time.LocalDate;

public class Incident {
    private Long id;
    private LocalDate dateIncident;
    private Long echeanceId;
    private int score;
    private TypeIncident typeIncident;
    private Long clientId;

    public Incident() {
        this.dateIncident = LocalDate.now();
    }

    public Incident(Long echeanceId, int score, TypeIncident typeIncident, Long clientId) {
        this.dateIncident = LocalDate.now();
        this.echeanceId = echeanceId;
        this.score = score;
        this.typeIncident = typeIncident;
        this.clientId = clientId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateIncident() {
        return dateIncident;
    }

    public void setDateIncident(LocalDate dateIncident) {
        this.dateIncident = dateIncident;
    }

    public Long getEcheanceId() {
        return echeanceId;
    }

    public void setEcheanceId(Long echeanceId) {
        this.echeanceId = echeanceId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public TypeIncident getTypeIncident() {
        return typeIncident;
    }

    public void setTypeIncident(TypeIncident typeIncident) {
        this.typeIncident = typeIncident;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
