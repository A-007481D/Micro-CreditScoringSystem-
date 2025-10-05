package com.microfin.model;

import com.microfin.enums.DecisionCredit;
import com.microfin.enums.TypeCredit;
import java.time.LocalDate;

public class Credit {
    private Long id;
    private Long clientId;
    private LocalDate dateDeCredit;
    private double montantDemande;
    private double montantOctroye;
    private double tauxInteret;
    private int dureeEnMois;
    private TypeCredit typeCredit;
    private DecisionCredit decision;
    private int scoreAuMomentDuCredit;
    private boolean actif;

    public Credit() {
        this.dateDeCredit = LocalDate.now();
        this.actif = true;
    }

    public Credit(Long clientId, double montantDemande, double montantOctroye, 
                  double tauxInteret, int dureeEnMois, TypeCredit typeCredit, 
                  DecisionCredit decision, int scoreAuMomentDuCredit) {
        this.clientId = clientId;
        this.dateDeCredit = LocalDate.now();
        this.montantDemande = montantDemande;
        this.montantOctroye = montantOctroye;
        this.tauxInteret = tauxInteret;
        this.dureeEnMois = dureeEnMois;
        this.typeCredit = typeCredit;
        this.decision = decision;
        this.scoreAuMomentDuCredit = scoreAuMomentDuCredit;
        this.actif = true;
    }

    public double calculerMensualite() {
        if (dureeEnMois == 0) return 0;
        double tauxMensuel = tauxInteret / 100 / 12;
        if (tauxMensuel == 0) return montantOctroye / dureeEnMois;
        
        return montantOctroye * (tauxMensuel * Math.pow(1 + tauxMensuel, dureeEnMois)) / 
               (Math.pow(1 + tauxMensuel, dureeEnMois) - 1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDate getDateDeCredit() {
        return dateDeCredit;
    }

    public void setDateDeCredit(LocalDate dateDeCredit) {
        this.dateDeCredit = dateDeCredit;
    }

    public double getMontantDemande() {
        return montantDemande;
    }

    public void setMontantDemande(double montantDemande) {
        this.montantDemande = montantDemande;
    }

    public double getMontantOctroye() {
        return montantOctroye;
    }

    public void setMontantOctroye(double montantOctroye) {
        this.montantOctroye = montantOctroye;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public int getDureeEnMois() {
        return dureeEnMois;
    }

    public void setDureeEnMois(int dureeEnMois) {
        this.dureeEnMois = dureeEnMois;
    }

    public TypeCredit getTypeCredit() {
        return typeCredit;
    }

    public void setTypeCredit(TypeCredit typeCredit) {
        this.typeCredit = typeCredit;
    }

    public DecisionCredit getDecision() {
        return decision;
    }

    public void setDecision(DecisionCredit decision) {
        this.decision = decision;
    }

    public int getScoreAuMomentDuCredit() {
        return scoreAuMomentDuCredit;
    }

    public void setScoreAuMomentDuCredit(int scoreAuMomentDuCredit) {
        this.scoreAuMomentDuCredit = scoreAuMomentDuCredit;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
