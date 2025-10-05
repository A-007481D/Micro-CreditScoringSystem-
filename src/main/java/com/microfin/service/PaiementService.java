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
import java.util.stream.Collectors;

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

    /**
     * Enregistre un paiement pour une échéance
     * @param echeanceId L'ID de l'échéance à payer
     * @param datePaiement La date du paiement
     * @param clientId L'ID du client
     * @param clientType Le type de client (EMPLOYE/PROFESSIONNEL)
     * @return true si le paiement a été enregistré avec succès, false sinon
     */
    public boolean enregistrerPaiement(Long echeanceId, LocalDate datePaiement, Long clientId, String clientType) {
        System.out.println("\n=== ENREGISTREMENT PAIEMENT ===");
        System.out.println("Échéance ID: " + echeanceId);
        System.out.println("Date de paiement: " + datePaiement);
        System.out.println("Client ID: " + clientId);
        System.out.println("Type client: " + clientType);
        
        // Récupération de l'échéance
        Optional<Echeance> echeanceOpt = echeanceRepository.findById(echeanceId);
        
        if (echeanceOpt.isEmpty()) {
            System.err.println("✗ Aucune échéance trouvée avec l'ID: " + echeanceId);
            return false;
        }
        
        Echeance echeance = echeanceOpt.get();
        System.out.println("Échéance trouvée: " + echeance);
        
        // Vérification si l'échéance est déjà payée
        if (echeance.getDateDePaiement() != null) {
            System.err.println("✗ Cette échéance a déjà été payée le " + echeance.getDateDePaiement());
            return false;
        }
        
        // Enregistrement du paiement
        echeance.enregistrerPaiement(datePaiement);
        System.out.println("Paiement enregistré en mémoire. Statut: " + echeance.getStatutPaiement());
        
        // Mise à jour dans la base de données
        boolean updated = echeanceRepository.update(echeance);
        
        if (!updated) {
            System.err.println("✗ Échec de la mise à jour de l'échéance dans la base de données");
            return false;
        }
        
        System.out.println("✓ Paiement enregistré avec succès pour l'échéance " + echeanceId);
        
        // Gestion des incidents si le paiement est en retard
        if (echeance.getStatutPaiement() != StatutPaiement.PAYE_A_TEMPS) {
            System.out.println("Traitement d'un incident pour un paiement en retard...");
            
            // Création d'un incident
            Incident incident = historiqueEngine.creerIncident(echeance, clientId);
            System.out.println("Incident créé: " + incident);
            
            // Sauvegarde de l'incident
            boolean incidentSauvegarde = incidentRepository.save(incident);
            
            if (incidentSauvegarde) {
                System.out.println("✓ Incident enregistré avec succès");
                
                // Mise à jour du score du client
                Optional<Personne> personneOpt = clientService.consulterClient(clientType, clientId);
                
                if (personneOpt.isPresent()) {
                    Personne personne = personneOpt.get();
                    System.out.println("Client trouvé: " + personne.getNom() + " " + personne.getPrenom());
                    
                    // Récupération de l'historique pour le calcul du score
                    List<Incident> incidents = incidentRepository.findByClientId(clientId);
                    List<Echeance> echeances = echeanceRepository.findByCreditId(echeance.getCreditId());
                    
                    // Calcul du nouveau score
                    int scoreHistorique = historiqueEngine.calculerScoreHistorique(incidents, echeances);
                    int ancienScore = personne.getScore();
                    personne.setScore(ancienScore + scoreHistorique);
                    
                    System.out.println("Mise à jour du score: " + ancienScore + " → " + personne.getScore());
                    
                    // Mise à jour du client
                    clientService.recalculerScore(personne);
                } else {
                    System.err.println("✗ Client non trouvé avec l'ID: " + clientId);
                }
            } else {
                System.err.println("✗ Échec de l'enregistrement de l'incident");
            }
        }
        
        System.out.println("=== FIN ENREGISTREMENT PAIEMENT ===\n");
        return true;
    }

    /**
     * Récupère la liste des échéances en retard pour un crédit
     * @param creditId L'ID du crédit
     * @return Liste des échéances en retard
     */
    public List<Echeance> listerEcheancesEnRetard(Long creditId) {
        // Utilise la méthode optimisée du repository
        List<Echeance> echeances = echeanceRepository.findEnRetardByCreditId(creditId);
        
        // Met à jour le statut de chaque échéance pour s'assurer qu'il est à jour
        for (Echeance echeance : echeances) {
            echeance.setStatutPaiement(echeance.determinerStatut());
        }
        
        return echeances;
    }

    /**
     * Récupère la liste des échéances impayées pour un crédit
     * @param creditId L'ID du crédit
     * @return Liste des échéances impayées
     */
    public List<Echeance> listerEcheancesImpayees(Long creditId) {
        // Utilise la méthode optimisée du repository
        List<Echeance> echeances = echeanceRepository.findImpayeesByCreditId(creditId);
        
        // Met à jour le statut de chaque échéance pour s'assurer qu'il est à jour
        for (Echeance echeance : echeances) {
            echeance.setStatutPaiement(echeance.determinerStatut());
        }
        
        return echeances;
    }
    
    /**
     * Récupère la liste des échéances à payer pour un crédit
     * @param creditId L'ID du crédit
     * @return Liste des échéances à payer
     */
    public List<Echeance> listerEcheancesAPayer(Long creditId) {
        // Utilise la méthode optimisée du repository
        List<Echeance> echeances = echeanceRepository.findAPayerByCreditId(creditId);
        
        // Met à jour le statut de chaque échéance pour s'assurer qu'il est à jour
        for (Echeance echeance : echeances) {
            echeance.setStatutPaiement(echeance.determinerStatut());
        }
        
        return echeances;
    }

    public List<Incident> consulterHistoriqueIncidents(Long clientId) {
        return incidentRepository.findByClientId(clientId);
    }

    public List<Incident> consulterIncidentsRecents(Long clientId, int mois) {
        return incidentRepository.findRecentByClientId(clientId, mois);
    }
}
