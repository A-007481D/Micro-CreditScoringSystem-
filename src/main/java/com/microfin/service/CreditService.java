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
        double mensualite = credit.calculerMensualite();
        LocalDate dateDebut = credit.getDateDeCredit();
        
        for (int i = 1; i <= credit.getDureeEnMois(); i++) {
            LocalDate dateEcheance = dateDebut.plusMonths(i);
            Echeance echeance = new Echeance(credit.getId(), dateEcheance, mensualite, i);
            echeanceRepository.save(echeance);
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
