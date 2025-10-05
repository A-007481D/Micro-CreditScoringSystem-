package com.microfin.service;

import com.microfin.engine.DecisionEngine;
import com.microfin.engine.ScoringEngine;
import com.microfin.enums.DecisionCredit;
import com.microfin.enums.TypeCredit;
import com.microfin.model.Credit;
import com.microfin.model.Echeance;
import com.microfin.model.Personne;
import com.microfin.repository.CreditRepository;
import com.microfin.repository.EcheanceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CreditService {
    
    private final CreditRepository creditRepository;
    private final EcheanceRepository echeanceRepository;
    private final ScoringEngine scoringEngine;
    private final DecisionEngine decisionEngine;

    public CreditService() {
        this.creditRepository = new CreditRepository();
        this.echeanceRepository = new EcheanceRepository();
        this.scoringEngine = new ScoringEngine();
        this.decisionEngine = new DecisionEngine();
    }

    public Credit demanderCredit(Personne personne, String clientType, double montantDemande, 
                                 int dureeEnMois, TypeCredit typeCredit) {
        
        int score = personne.getScore();
        double capaciteEmprunt = scoringEngine.calculerCapaciteEmprunt(personne, score);
        
        DecisionCredit decision = decisionEngine.prendreDecision(personne, score, montantDemande, capaciteEmprunt);
        
        double montantOctroye = 0;
        double tauxInteret = 0;
        
        if (decision == DecisionCredit.ACCORD_IMMEDIAT) {
            montantOctroye = montantDemande;
            tauxInteret = decisionEngine.calculerTauxInteret(score, decision);
        } else if (decision == DecisionCredit.ETUDE_MANUELLE) {
            montantOctroye = Math.min(montantDemande, capaciteEmprunt);
            tauxInteret = decisionEngine.calculerTauxInteret(score, decision);
        }
        
        Credit credit = new Credit(
            personne.getId(),
            montantDemande,
            montantOctroye,
            tauxInteret,
            dureeEnMois,
            typeCredit,
            decision,
            score
        );
        
        Credit creditSauvegarde = creditRepository.save(credit, clientType);
        
        if (creditSauvegarde != null && decision == DecisionCredit.ACCORD_IMMEDIAT) {
            genererEcheances(creditSauvegarde);
        }
        
        return creditSauvegarde;
    }

    public void genererEcheances(Credit credit) {
        try {
            System.out.println("=== DÉBUT génération des échéances pour le crédit ID: " + credit.getId() + " ===");
            
            // Vérification des données du crédit
            if (credit == null) {
                System.err.println("✗ Erreur: Le crédit est null");
                return;
            }
            
            // Calcul de la mensualité
            double mensualite = credit.calculerMensualite();
            if (mensualite <= 0) {
                System.err.println("✗ Erreur: La mensualité calculée est invalide: " + mensualite);
                System.err.println("Détails du calcul: montant=" + credit.getMontantOctroye() + 
                                 ", taux=" + credit.getTauxInteret() + 
                                 ", duree=" + credit.getDureeEnMois() + " mois");
            }
            
            // Vérification de la date de début
            LocalDate dateDebut = credit.getDateDeCredit();
            if (dateDebut == null) {
                System.err.println("✗ La date de début du crédit n'est pas définie. Utilisation de la date actuelle.");
                dateDebut = LocalDate.now();
                credit.setDateDeCredit(dateDebut);
            }
            
            // Affichage des informations de débogage
            System.out.println("Détails du crédit:");
            System.out.println("- Montant octroyé: " + credit.getMontantOctroye());
            System.out.println("- Taux d'intérêt: " + credit.getTauxInteret() + "%");
            System.out.println("- Durée: " + credit.getDureeEnMois() + " mois");
            System.out.println("- Mensualité calculée: " + String.format("%.2f", mensualite) + " DH");
            System.out.println("- Date de début: " + dateDebut);
            
            // Vérification du nombre d'échéances
            int nbEcheances = credit.getDureeEnMois();
            if (nbEcheances <= 0) {
                System.err.println("✗ Erreur: Le nombre d'échéances doit être supérieur à 0");
                return;
            }
            
            // Génération des échéances
            int echeancesCreees = 0;
            for (int i = 1; i <= nbEcheances; i++) {
                try {
                    LocalDate dateEcheance = dateDebut.plusMonths(i);
                    System.out.println("\nCréation échéance " + i + "/" + nbEcheances + " pour le " + dateEcheance);
                    
                    Echeance echeance = new Echeance(credit.getId(), dateEcheance, mensualite, i);
                    System.out.println("Échéance créée en mémoire: " + echeance);
                    
                    Echeance savedEcheance = echeanceRepository.save(echeance);
                    
                    if (savedEcheance == null || savedEcheance.getId() == null) {
                        System.err.println("✗ Échec de la sauvegarde de l'échéance " + i);
                    } else {
                        System.out.println("✓ Échéance " + i + " créée avec succès (ID: " + savedEcheance.getId() + ")");
                        echeancesCreees++;
                    }
                } catch (Exception e) {
                    System.err.println("✗ Erreur lors de la création de l'échéance " + i + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Vérification finale
            List<Echeance> echeancesCreeesList = echeanceRepository.findByCreditId(credit.getId());
            System.out.println("\n=== SYNTHÈSE ===");
            System.out.println("Échéances à créer: " + nbEcheances);
            System.out.println("Échéances créées avec succès: " + echeancesCreees);
            System.out.println("Échéances trouvées en base: " + echeancesCreeesList.size());
            
            if (echeancesCreeesList.size() < nbEcheances) {
                System.err.println("✗ Attention: Le nombre d'échéances créées ne correspond pas au nombre attendu");
            }
            
            System.out.println("=== FIN génération des échéances pour le crédit ID: " + credit.getId() + " ===\n");
            
        } catch (Exception e) {
            System.err.println("✗ ERREUR CRITIQUE lors de la génération des échéances: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Optional<Credit> consulterCredit(Long creditId) {
        return creditRepository.findById(creditId);
    }

    public List<Credit> listerCreditsClient(Long clientId) {
        return creditRepository.findByClientId(clientId);
    }

    public List<Credit> listerCreditsActifsClient(Long clientId) {
        return creditRepository.findActiveByClientId(clientId);
    }

    public List<Echeance> listerEcheances(Long creditId) {
        return echeanceRepository.findByCreditId(creditId);
    }
    
    /**
     * Récupère tous les crédits
     * @return Liste de tous les crédits
     */
    public List<Credit> listerCredits() {
        return creditRepository.findAll();
    }
    
    /**
     * Manually approve or reject a credit that's in ETUDE_MANUELLE status
     * @param creditId ID of the credit to process
     * @param approuve true to approve, false to reject
     * @param montantApprouve Amount approved (if approving)
     * @return true if operation was successful
     */
    public boolean traiterDemandeManuelle(Long creditId, boolean approuve, double montantApprouve) {
        Optional<Credit> creditOpt = creditRepository.findById(creditId);
        if (!creditOpt.isPresent()) {
            System.err.println("✗ Crédit non trouvé avec l'ID: " + creditId);
            return false;
        }

        Credit credit = creditOpt.get();
        if (credit.getDecision() != DecisionCredit.ETUDE_MANUELLE) {
            System.err.println("✗ Seuls les crédits en attente d'étude manuelle peuvent être traités");
            return false;
        }

        if (approuve) {
            if (montantApprouve <= 0 || montantApprouve > credit.getMontantDemande()) {
                System.err.println("✗ Montant approuvé invalide");
                return false;
            }
            
            // Update credit with approved amount and set to ACCORDE
            credit.setMontantOctroye(montantApprouve);
            credit.setDecision(DecisionCredit.ACCORDE);
            credit.setActif(true);
            
            // Save the updated credit
            if (creditRepository.update(credit)) {
                // Generate payment installments
                genererEcheances(credit);
                System.out.println("✓ Crédit approuvé avec succès! Les échéances ont été générées.");
                return true;
            }
        } else {
            // Reject the credit
            credit.setDecision(DecisionCredit.REFUSE);
            credit.setActif(false);
            boolean updated = creditRepository.update(credit);
            if (updated) {
                System.out.println("✓ Crédit refusé avec succès.");
            }
            return updated;
        }
        
        return false;
    }

    public boolean cloturerCredit(Long creditId) {
        Optional<Credit> creditOpt = creditRepository.findById(creditId);
        if (creditOpt.isPresent()) {
            Credit credit = creditOpt.get();
            credit.setActif(false);
            return creditRepository.update(credit);
        }
        return false;
    }
}
