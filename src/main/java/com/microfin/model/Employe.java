package com.microfin.model;

import com.microfin.enums.Secteur;
import com.microfin.enums.SituationFamiliale;
import com.microfin.enums.TypeContrat;
import java.time.LocalDate;

public class Employe extends Personne {
    private double salaire;
    private int anciennete;
    private String poste;
    private TypeContrat typeContrat;
    private Secteur secteur;

    public Employe() {
        super();
    }

    public Employe(String nom, String prenom, LocalDate dateDeNaissance, String ville,
                   int nombreEnfants, double investissement, double placement,
                   SituationFamiliale situationFamiliale, double salaire, int anciennete,
                   String poste, TypeContrat typeContrat, Secteur secteur) {
        super(nom, prenom, dateDeNaissance, ville, nombreEnfants, investissement, placement, situationFamiliale);
        this.salaire = salaire;
        this.anciennete = anciennete;
        this.poste = poste;
        this.typeContrat = typeContrat;
        this.secteur = secteur;
    }

    @Override
    public double getRevenuMensuel() {
        return salaire;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public int getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(int anciennete) {
        this.anciennete = anciennete;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Secteur getSecteur() {
        return secteur;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
    }

    public int getAncienneteEnAnnees() {
        return anciennete / 12;
    }
}
