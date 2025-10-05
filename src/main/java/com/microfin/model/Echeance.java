package com.microfin.model;

import com.microfin.enums.StatutPaiement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Echeance {
    private Long id;
    private Long creditId;
    private LocalDate dateEcheance;
    private double mensualite;
    private LocalDate dateDePaiement;
    private StatutPaiement statutPaiement;
    private int numeroEcheance;

    public Echeance() {
        this.statutPaiement = StatutPaiement.EN_RETARD;
    }

    public Echeance(Long creditId, LocalDate dateEcheance, double mensualite, int numeroEcheance) {
        this.creditId = creditId;
        this.dateEcheance = dateEcheance;
        this.mensualite = mensualite;
        this.numeroEcheance = numeroEcheance;
        this.statutPaiement = StatutPaiement.EN_RETARD;
    }

    public void enregistrerPaiement(LocalDate datePaiement) {
        this.dateDePaiement = datePaiement;
        this.statutPaiement = determinerStatut();
    }

    /**
     * Détermine le statut actuel de l'échéance en fonction de la date du jour
     * @return Le statut de paiement actuel
     */
    public StatutPaiement determinerStatut() {
        // Si l'échéance a été payée
        if (dateDePaiement != null) {
            long joursRetard = ChronoUnit.DAYS.between(dateEcheance, dateDePaiement);
            if (joursRetard <= 0) {
                return StatutPaiement.PAYE_A_TEMPS;
            } else if (joursRetard >= 31) {
                return StatutPaiement.IMPAYE_REGLE;
            } else if (joursRetard >= 5) {
                return StatutPaiement.PAYE_EN_RETARD;
            } else {
                return StatutPaiement.PAYE_A_TEMPS;
            }
        }
        
        // Si l'échéance n'a pas encore été payée
        LocalDate aujourdhui = LocalDate.now();
        
        // Si la date d'échéance est dans le futur
        if (aujourdhui.isBefore(dateEcheance.plusDays(1))) {
            return StatutPaiement.A_PAYER;
        }
        
        // Calculer le nombre de jours de retard
        long joursRetard = ChronoUnit.DAYS.between(dateEcheance, aujourdhui);
        
        if (joursRetard >= 31) {
            return StatutPaiement.IMPAYE_NON_REGLE;
        } else if (joursRetard >= 5) {
            return StatutPaiement.EN_RETARD;
        } else {
            // Moins de 5 jours de retard, toujours considéré comme à payer
            return StatutPaiement.A_PAYER;
        }
    }

    /**
     * Vérifie si l'échéance est en retard
     * @return true si l'échéance est en retard, false sinon
     */
    public boolean estEnRetard() {
        if (dateDePaiement != null) return false; // Déjà payée
        return LocalDate.now().isAfter(dateEcheance.plusDays(4));
    }

    /**
     * Vérifie si l'échéance est impayée (plus de 30 jours de retard)
     * @return true si l'échéance est impayée, false sinon
     */
    public boolean estImpaye() {
        if (dateDePaiement != null) return false; // Déjà payée
        return LocalDate.now().isAfter(dateEcheance.plusDays(30));
    }
    
    /**
     * Vérifie si l'échéance est à payer (non échue ou moins de 5 jours de retard)
     * @return true si l'échéance est à payer, false sinon
     */
    public boolean estAPayer() {
        if (dateDePaiement != null) return false; // Déjà payée
        LocalDate aujourdhui = LocalDate.now();
        return !aujourdhui.isAfter(dateEcheance.plusDays(4));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreditId() {
        return creditId;
    }

    public void setCreditId(Long creditId) {
        this.creditId = creditId;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public double getMensualite() {
        return mensualite;
    }

    public void setMensualite(double mensualite) {
        this.mensualite = mensualite;
    }

    public LocalDate getDateDePaiement() {
        return dateDePaiement;
    }

    public void setDateDePaiement(LocalDate dateDePaiement) {
        this.dateDePaiement = dateDePaiement;
        this.statutPaiement = determinerStatut();
    }

    public StatutPaiement getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public int getNumeroEcheance() {
        return numeroEcheance;
    }

    public void setNumeroEcheance(int numeroEcheance) {
        this.numeroEcheance = numeroEcheance;
    }
}
