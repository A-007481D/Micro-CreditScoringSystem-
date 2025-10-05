package com.microfin.engine;

import com.microfin.enums.StatutPaiement;
import com.microfin.enums.TypeIncident;
import com.microfin.model.Echeance;
import com.microfin.model.Incident;

import java.util.List;
import java.util.stream.Collectors;

public class HistoriqueEngine {

    public int calculerScoreHistorique(List<Incident> incidents, List<Echeance> echeances) {
        int score = 0;
        
        long impayesNonRegles = incidents.stream()
            .filter(i -> i.getTypeIncident() == TypeIncident.IMPAYE_NON_REGLE)
            .count();
        score += (int) (impayesNonRegles * -10);
        
        long impayesRegles = incidents.stream()
            .filter(i -> i.getTypeIncident() == TypeIncident.IMPAYE_REGLE)
            .count();
        score += (int) (impayesRegles * 5);
        
        long retards = incidents.stream()
            .filter(i -> i.getTypeIncident() == TypeIncident.EN_RETARD)
            .count();
        
        if (retards >= 4) {
            score -= 5;
        } else if (retards >= 1) {
            score -= 3;
        }
        
        long retardsPayes = incidents.stream()
            .filter(i -> i.getTypeIncident() == TypeIncident.PAYE_EN_RETARD)
            .count();
        score += (int) (retardsPayes * 3);
        
        boolean toutesEcheancesPayees = echeances.stream()
            .allMatch(e -> e.getDateDePaiement() != null);
        
        boolean aucunIncident = echeances.stream()
            .allMatch(e -> e.getStatutPaiement() == StatutPaiement.PAYE_A_TEMPS);
        
        if (toutesEcheancesPayees && aucunIncident && !echeances.isEmpty()) {
            score += 10;
        }
        
        return score;
    }

    public TypeIncident determinerTypeIncident(StatutPaiement statut) {
        return switch (statut) {
            case PAYE_A_TEMPS -> TypeIncident.PAYE_A_TEMPS;
            case EN_RETARD -> TypeIncident.EN_RETARD;
            case PAYE_EN_RETARD -> TypeIncident.PAYE_EN_RETARD;
            case IMPAYE_NON_REGLE -> TypeIncident.IMPAYE_NON_REGLE;
            case IMPAYE_REGLE -> TypeIncident.IMPAYE_REGLE;
        };
    }

    public Incident creerIncident(Echeance echeance, Long clientId) {
        TypeIncident typeIncident = determinerTypeIncident(echeance.getStatutPaiement());
        int scoreImpact = echeance.getStatutPaiement().getScoreImpact();
        
        return new Incident(echeance.getId(), scoreImpact, typeIncident, clientId);
    }
}
