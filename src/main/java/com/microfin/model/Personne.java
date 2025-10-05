package com.microfin.model;

import com.microfin.enums.SituationFamiliale;
import java.time.LocalDate;

public abstract class Personne {
    protected Long id;
    protected String nom;
    protected String prenom;
    protected LocalDate dateDeNaissance;
    protected String ville;
    protected int nombreEnfants;
    protected double investissement;
    protected double placement;
    protected SituationFamiliale situationFamiliale;
    protected LocalDate createdAt;
    protected int score;
    protected boolean clientExistant;

    public Personne() {
        this.createdAt = LocalDate.now();
        this.score = 0;
        this.clientExistant = false;
    }

    public Personne(String nom, String prenom, LocalDate dateDeNaissance, String ville, 
                    int nombreEnfants, double investissement, double placement, 
                    SituationFamiliale situationFamiliale) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.ville = ville;
        this.nombreEnfants = nombreEnfants;
        this.investissement = investissement;
        this.placement = placement;
        this.situationFamiliale = situationFamiliale;
        this.createdAt = LocalDate.now();
        this.score = 0;
        this.clientExistant = false;
    }

    public abstract double getRevenuMensuel();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getNombreEnfants() {
        return nombreEnfants;
    }

    public void setNombreEnfants(int nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    public double getInvestissement() {
        return investissement;
    }

    public void setInvestissement(double investissement) {
        this.investissement = investissement;
    }

    public double getPlacement() {
        return placement;
    }

    public void setPlacement(double placement) {
        this.placement = placement;
    }

    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isClientExistant() {
        return clientExistant;
    }

    public void setClientExistant(boolean clientExistant) {
        this.clientExistant = clientExistant;
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateDeNaissance.getYear();
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
