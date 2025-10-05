package com.microfin.model;

import com.microfin.enums.SecteurActivite;
import com.microfin.enums.SituationFamiliale;
import java.time.LocalDate;

public class Professionnel extends Personne {
    private double revenu;
    private String immatriculationFiscale;
    private SecteurActivite secteurActivite;
    private String activite;

    public Professionnel() {
        super();
    }

    public Professionnel(String nom, String prenom, LocalDate dateDeNaissance, String ville,
                         int nombreEnfants, double investissement, double placement,
                         SituationFamiliale situationFamiliale, double revenu,
                         String immatriculationFiscale, SecteurActivite secteurActivite, String activite) {
        super(nom, prenom, dateDeNaissance, ville, nombreEnfants, investissement, placement, situationFamiliale);
        this.revenu = revenu;
        this.immatriculationFiscale = immatriculationFiscale;
        this.secteurActivite = secteurActivite;
        this.activite = activite;
    }

    @Override
    public double getRevenuMensuel() {
        return revenu;
    }

    public double getRevenu() {
        return revenu;
    }

    public void setRevenu(double revenu) {
        this.revenu = revenu;
    }

    public String getImmatriculationFiscale() {
        return immatriculationFiscale;
    }

    public void setImmatriculationFiscale(String immatriculationFiscale) {
        this.immatriculationFiscale = immatriculationFiscale;
    }

    public SecteurActivite getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(SecteurActivite secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }
}
