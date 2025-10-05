package com.microfin.service;

import com.microfin.enums.Secteur;
import com.microfin.enums.SituationFamiliale;
import com.microfin.enums.TypeContrat;
import com.microfin.model.Employe;
import com.microfin.model.Incident;
import com.microfin.model.Personne;
import com.microfin.repository.CreditRepository;
import com.microfin.repository.IncidentRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    
    private final ClientService clientService;
    private final CreditRepository creditRepository;
    private final IncidentRepository incidentRepository;

    public AnalyticsService() {
        this.clientService = new ClientService();
        this.creditRepository = new CreditRepository();
        this.incidentRepository = new IncidentRepository();
    }

    public List<Personne> rechercherClientsEligiblesCreditImmobilier() {
        return clientService.listerTousLesClients().stream()
            .filter(p -> p instanceof Employe)
            .map(p -> (Employe) p)
            .filter(e -> {
                int age = e.getAge();
                return age >= 25 && age <= 50;
            })
            .filter(e -> e.getRevenuMensuel() > 4000)
            .filter(e -> e.getTypeContrat() == TypeContrat.CDI)
            .filter(e -> e.getScore() > 70)
            .filter(e -> e.getSituationFamiliale() == SituationFamiliale.MARIE)
            .collect(Collectors.toList());
    }

    public List<Personne> rechercherClientsARisque() {
        return clientService.listerTousLesClients().stream()
            .filter(p -> p.getScore() < 60)
            .filter(p -> {
                List<Incident> incidents = incidentRepository.findRecentByClientId(p.getId(), 6);
                return !incidents.isEmpty();
            })
            .sorted(Comparator.comparingInt(Personne::getScore).reversed())
            .limit(10)
            .collect(Collectors.toList());
    }

    public List<Personne> trierParScore() {
        return clientService.listerTousLesClients().stream()
            .sorted(Comparator.comparingInt(Personne::getScore).reversed())
            .collect(Collectors.toList());
    }

    public List<Personne> trierParRevenus() {
        return clientService.listerTousLesClients().stream()
            .sorted(Comparator.comparingDouble(Personne::getRevenuMensuel).reversed())
            .collect(Collectors.toList());
    }

    public List<Personne> trierParAnciennete() {
        return clientService.listerTousLesClients().stream()
            .sorted(Comparator.comparing(Personne::getCreatedAt))
            .collect(Collectors.toList());
    }

    public Map<String, Map<String, Object>> repartitionParTypeEmploi() {
        Map<String, List<Employe>> groupes = clientService.listerEmployes().stream()
            .collect(Collectors.groupingBy(e -> 
                e.getTypeContrat().name() + "_" + e.getSecteur().name()
            ));

        Map<String, Map<String, Object>> statistiques = new HashMap<>();

        for (Map.Entry<String, List<Employe>> entry : groupes.entrySet()) {
            List<Employe> employes = entry.getValue();
            
            double scoreMoyen = employes.stream()
                .mapToInt(Employe::getScore)
                .average()
                .orElse(0);
            
            double revenuMoyen = employes.stream()
                .mapToDouble(Employe::getRevenuMensuel)
                .average()
                .orElse(0);
            
            long nombreCreditsApprouves = employes.stream()
                .filter(e -> creditRepository.findByClientId(e.getId()).stream()
                    .anyMatch(c -> c.getDecision().name().contains("ACCORD")))
                .count();
            
            double tauxApprobation = employes.isEmpty() ? 0 : 
                (nombreCreditsApprouves * 100.0 / employes.size());

            Map<String, Object> stats = new HashMap<>();
            stats.put("nombre", employes.size());
            stats.put("scoreMoyen", scoreMoyen);
            stats.put("revenuMoyen", revenuMoyen);
            stats.put("tauxApprobation", tauxApprobation);
            
            statistiques.put(entry.getKey(), stats);
        }

        return statistiques;
    }

    public List<Personne> selectionnerClientsPourCampagne() {
        return clientService.listerTousLesClients().stream()
            .filter(p -> p.getScore() >= 65 && p.getScore() <= 85)
            .filter(p -> p.getRevenuMensuel() >= 4000 && p.getRevenuMensuel() <= 8000)
            .filter(p -> {
                int age = p.getAge();
                return age >= 28 && age <= 45;
            })
            .filter(p -> creditRepository.findActiveByClientId(p.getId()).isEmpty())
            .collect(Collectors.toList());
    }

    public List<Personne> rechercherParCriteres(Integer scoreMin, Integer scoreMax,
                                                 Double revenuMin, Double revenuMax,
                                                 Integer ageMin, Integer ageMax) {
        return clientService.listerTousLesClients().stream()
            .filter(p -> scoreMin == null || p.getScore() >= scoreMin)
            .filter(p -> scoreMax == null || p.getScore() <= scoreMax)
            .filter(p -> revenuMin == null || p.getRevenuMensuel() >= revenuMin)
            .filter(p -> revenuMax == null || p.getRevenuMensuel() <= revenuMax)
            .filter(p -> ageMin == null || p.getAge() >= ageMin)
            .filter(p -> ageMax == null || p.getAge() <= ageMax)
            .collect(Collectors.toList());
    }
}
