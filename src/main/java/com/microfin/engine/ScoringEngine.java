package com.microfin.engine;

import com.microfin.enums.Secteur;
import com.microfin.enums.TypeContrat;
import com.microfin.model.Employe;
import com.microfin.model.Personne;
import com.microfin.model.Professionnel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ScoringEngine {

    public int calculerScore(Personne personne) {
        int scoreTotal = 0;
        
        scoreTotal += calculerStabiliteProfessionnelle(personne);
        scoreTotal += calculerCapaciteFinanciere(personne);
        scoreTotal += calculerRelationClient(personne);
        scoreTotal += calculerCriteresComplementaires(personne);
        
        return Math.min(100, Math.max(0, scoreTotal));
    }

    private int calculerStabiliteProfessionnelle(Personne personne) {
        int score = 0;
        
        if (personne instanceof Employe) {
            Employe employe = (Employe) personne;
            
            if (employe.getTypeContrat() == TypeContrat.CDI) {
                score += employe.getSecteur().getScoreStabilite();
            } else if (employe.getTypeContrat() == TypeContrat.CDD || 
                       employe.getTypeContrat() == TypeContrat.INTERIM) {
                score += 10;
            }
            
            int ancienneteAnnees = employe.getAnciennete() / 12;
            if (ancienneteAnnees >= 5) {
                score += 5;
            } else if (ancienneteAnnees >= 2) {
                score += 3;
            } else if (ancienneteAnnees >= 1) {
                score += 1;
            }
            
        } else if (personne instanceof Professionnel) {
            Professionnel prof = (Professionnel) personne;
            score += 18;
        }
        
        return score;
    }

    private int calculerCapaciteFinanciere(Personne personne) {
        double revenu = personne.getRevenuMensuel();
        
        if (revenu >= 10000) {
            return 30;
        } else if (revenu >= 8000) {
            return 25;
        } else if (revenu >= 5000) {
            return 20;
        } else if (revenu >= 3000) {
            return 15;
        } else {
            return 10;
        }
    }

    private int calculerRelationClient(Personne personne) {
        int score = 0;
        
        if (personne.isClientExistant()) {
            int moisAnciennete = (int) ChronoUnit.MONTHS.between(personne.getCreatedAt(), LocalDate.now());
            int anneesAnciennete = moisAnciennete / 12;
            
            if (anneesAnciennete > 3) {
                score += 10;
            } else if (anneesAnciennete >= 1) {
                score += 8;
            } else {
                score += 5;
            }
        } else {
            int age = personne.getAge();
            if (age >= 36 && age <= 55) {
                score += 10;
            } else if (age >= 26 && age <= 35) {
                score += 8;
            } else if (age >= 18 && age <= 25) {
                score += 4;
            } else {
                score += 6;
            }
            
            score += personne.getSituationFamiliale().getScoreRelation();
            
            if (personne.getNombreEnfants() == 0) {
                score += 2;
            } else if (personne.getNombreEnfants() <= 2) {
                score += 1;
            }
        }
        
        return score;
    }

    private int calculerCriteresComplementaires(Personne personne) {
        if (personne.getInvestissement() > 0 || personne.getPlacement() > 0) {
            return 10;
        }
        return 0;
    }

    public double calculerCapaciteEmprunt(Personne personne, int score) {
        double revenu = personne.getRevenuMensuel();
        
        if (personne.isClientExistant()) {
            if (score > 80) {
                return revenu * 10;
            } else if (score >= 60) {
                return revenu * 7;
            }
        } else {
            if (score >= 70) {
                return revenu * 4;
            }
        }
        
        return 0;
    }
}
