package com.microfin.service;

import com.microfin.engine.HistoriqueEngine;
import com.microfin.enums.StatutPaiement;
import com.microfin.model.Echeance;
import com.microfin.model.Incident;
import com.microfin.model.Personne;
import com.microfin.repository.EcheanceRepository;
import com.microfin.repository.IncidentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaiementService {
    
    private final EcheanceRepository echeanceRepository;
    private final IncidentRepository incidentRepository;
    private final HistoriqueEngine historiqueEngine;
    private final ClientService clientService;

    public PaiementService() {
        this.echeanceRepository = new EcheanceRepository();
        this.incidentRepository = new IncidentRepository();
        this.historiqueEngine = new HistoriqueEngine();
        this.clientService = new ClientService();
    }

    public boolean enregistrerPaiement(Long echeanceId, LocalDate datePaiement, Long clientId, String clientType) {
        Optional<Echeance> echeanceOpt = echeanceRepository.findById(echeanceId);
        
        if (echeanceOpt.isEmpty()) {
            return false;
        }
        
        Echeance echeance = echeanceOpt.get();
        echeance.enregistrerPaiement(datePaiement);
        
        boolean updated = echeanceRepository.update(echeance);
        
        if (updated && echeance.getStatutPaiement() != StatutPaiement.PAYE_A_TEMPS) {
            Incident incident = historiqueEngine.creerIncident(echeance, clientId);
            incidentRepository.save(incident);
            
            Optional<Personne> personneOpt = clientService.consulterClient(clientType, clientId);
            if (personneOpt.isPresent()) {
                Personne personne = personneOpt.get();
                List<Incident> incidents = incidentRepository.findByClientId(clientId);
                List<Echeance> echeances = echeanceRepository.findByCreditId(echeance.getCreditId());
                
                int scoreHistorique = historiqueEngine.calculerScoreHistorique(incidents, echeances);
                personne.setScore(personne.getScore() + scoreHistorique);
                clientService.recalculerScore(personne);
            }
        }
        
        return updated;
    }

    public List<Echeance> listerEcheancesEnRetard(Long creditId) {
        return echeanceRepository.findByCreditId(creditId).stream()
            .filter(Echeance::estEnRetard)
            .toList();
    }

    public List<Echeance> listerEcheancesImpayees(Long creditId) {
        return echeanceRepository.findByCreditId(creditId).stream()
            .filter(Echeance::estImpaye)
            .toList();
    }

    public List<Incident> consulterHistoriqueIncidents(Long clientId) {
        return incidentRepository.findByClientId(clientId);
    }

    public List<Incident> consulterIncidentsRecents(Long clientId, int mois) {
        return incidentRepository.findRecentByClientId(clientId, mois);
    }
}
