package com.microfin.engine;

import com.microfin.enums.DecisionCredit;
import com.microfin.model.Personne;

public class DecisionEngine {

    public DecisionCredit prendreDecision(Personne personne, int score, double montantDemande, double capaciteEmprunt) {
        
        if (montantDemande > capaciteEmprunt) {
            return DecisionCredit.REFUS_AUTOMATIQUE;
        }
        
        if (personne.isClientExistant()) {
            if (score >= 80) {
                return DecisionCredit.ACCORD_IMMEDIAT;
            } else if (score >= 60) {
                return DecisionCredit.ETUDE_MANUELLE;
            } else {
                return DecisionCredit.REFUS_AUTOMATIQUE;
            }
        } else {
            if (score >= 80) {
                return DecisionCredit.ACCORD_IMMEDIAT;
            } else if (score >= 70) {
                return DecisionCredit.ETUDE_MANUELLE;
            } else {
                return DecisionCredit.REFUS_AUTOMATIQUE;
            }
        }
    }

    public double calculerTauxInteret(int score, DecisionCredit decision) {
        if (decision == DecisionCredit.REFUS_AUTOMATIQUE) {
            return 0;
        }
        
        if (score >= 80) {
            return 4.5;
        } else if (score >= 70) {
            return 5.5;
        } else if (score >= 60) {
            return 6.5;
        } else {
            return 7.5;
        }
    }
}
