package com.microfin.view;

import com.microfin.model.Personne;
import com.microfin.service.AnalyticsService;
import com.microfin.util.FormatUtil;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuAnalytics {
    
    private final Scanner scanner;
    private final AnalyticsService analyticsService;

    public MenuAnalytics(Scanner scanner, AnalyticsService analyticsService) {
        this.scanner = scanner;
        this.analyticsService = analyticsService;
    }

    public void afficher() {
        boolean continuer = true;
        
        while (continuer) {
            FormatUtil.afficherTitre("Analytics & Recherche");
            System.out.println("1. Clients éligibles crédit immobilier");
            System.out.println("2. Clients à risque (Top 10)");
            System.out.println("3. Trier par score");
            System.out.println("4. Trier par revenus");
            System.out.println("5. Trier par ancienneté");
            System.out.println("6. Répartition par type d'emploi");
            System.out.println("7. Sélection pour campagne marketing");
            System.out.println("8. Recherche personnalisée");
            System.out.println("0. Retour");
            FormatUtil.afficherSeparateur();
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    clientsEligiblesImmobilier();
                    break;
                case 2:
                    clientsARisque();
                    break;
                case 3:
                    trierParScore();
                    break;
                case 4:
                    trierParRevenus();
                    break;
                case 5:
                    trierParAnciennete();
                    break;
                case 6:
                    repartitionParTypeEmploi();
                    break;
                case 7:
                    selectionCampagne();
                    break;
                case 8:
                    recherchePersonnalisee();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("✗ Choix invalide!");
                    break;
            }
        }
    }

    private void clientsEligiblesImmobilier() {
        List<Personne> clients = analyticsService.rechercherClientsEligiblesCreditImmobilier();
        
        FormatUtil.afficherTitre("Clients Éligibles Crédit Immobilier (" + clients.size() + ")");
        System.out.println("Critères: Âge 25-50, Revenus >4000 DH, CDI, Score >70, Marié");
        FormatUtil.afficherSeparateur();
        
        afficherListeClients(clients);
    }

    private void clientsARisque() {
        List<Personne> clients = analyticsService.rechercherClientsARisque();
        
        FormatUtil.afficherTitre("Clients à Risque - Top 10");
        System.out.println("Critères: Score <60, Incidents récents (<6 mois)");
        FormatUtil.afficherSeparateur();
        
        afficherListeClients(clients);
    }

    private void trierParScore() {
        List<Personne> clients = analyticsService.trierParScore();
        
        FormatUtil.afficherTitre("Clients Triés par Score (Décroissant)");
        afficherListeClients(clients);
    }

    private void trierParRevenus() {
        List<Personne> clients = analyticsService.trierParRevenus();
        
        FormatUtil.afficherTitre("Clients Triés par Revenus (Décroissant)");
        afficherListeClients(clients);
    }

    private void trierParAnciennete() {
        List<Personne> clients = analyticsService.trierParAnciennete();
        
        FormatUtil.afficherTitre("Clients Triés par Ancienneté");
        afficherListeClients(clients);
    }

    private void repartitionParTypeEmploi() {
        Map<String, Map<String, Object>> stats = analyticsService.repartitionParTypeEmploi();
        
        FormatUtil.afficherTitre("Répartition par Type d'Emploi");
        
        for (Map.Entry<String, Map<String, Object>> entry : stats.entrySet()) {
            System.out.println("\n" + entry.getKey() + ":");
            Map<String, Object> data = entry.getValue();
            System.out.println("  Nombre clients: " + data.get("nombre"));
            System.out.println("  Score moyen: " + String.format("%.1f", data.get("scoreMoyen")));
            System.out.println("  Revenu moyen: " + String.format("%.2f DH", data.get("revenuMoyen")));
            System.out.println("  Taux approbation: " + String.format("%.1f%%", data.get("tauxApprobation")));
        }
    }

    private void selectionCampagne() {
        List<Personne> clients = analyticsService.selectionnerClientsPourCampagne();
        
        FormatUtil.afficherTitre("Sélection Campagne Marketing (" + clients.size() + ")");
        System.out.println("Critères: Score 65-85, Revenus 4000-8000 DH, Âge 28-45, Pas de crédit actif");
        FormatUtil.afficherSeparateur();
        
        afficherListeClients(clients);
        
        System.out.println("\n✓ " + clients.size() + " clients sélectionnés pour la campagne!");
    }

    private void recherchePersonnalisee() {
        FormatUtil.afficherTitre("Recherche Personnalisée");
        
        System.out.print("Score minimum (ou Enter pour ignorer): ");
        String scoreMinStr = scanner.nextLine();
        Integer scoreMin = scoreMinStr.isEmpty() ? null : Integer.parseInt(scoreMinStr);
        
        System.out.print("Score maximum (ou Enter pour ignorer): ");
        String scoreMaxStr = scanner.nextLine();
        Integer scoreMax = scoreMaxStr.isEmpty() ? null : Integer.parseInt(scoreMaxStr);
        
        System.out.print("Revenu minimum (ou Enter pour ignorer): ");
        String revMinStr = scanner.nextLine();
        Double revenuMin = revMinStr.isEmpty() ? null : Double.parseDouble(revMinStr);
        
        System.out.print("Revenu maximum (ou Enter pour ignorer): ");
        String revMaxStr = scanner.nextLine();
        Double revenuMax = revMaxStr.isEmpty() ? null : Double.parseDouble(revMaxStr);
        
        System.out.print("Âge minimum (ou Enter pour ignorer): ");
        String ageMinStr = scanner.nextLine();
        Integer ageMin = ageMinStr.isEmpty() ? null : Integer.parseInt(ageMinStr);
        
        System.out.print("Âge maximum (ou Enter pour ignorer): ");
        String ageMaxStr = scanner.nextLine();
        Integer ageMax = ageMaxStr.isEmpty() ? null : Integer.parseInt(ageMaxStr);
        
        List<Personne> clients = analyticsService.rechercherParCriteres(
            scoreMin, scoreMax, revenuMin, revenuMax, ageMin, ageMax);
        
        FormatUtil.afficherTitre("Résultats (" + clients.size() + ")");
        afficherListeClients(clients);
    }

    private void afficherListeClients(List<Personne> clients) {
        for (Personne p : clients) {
            System.out.printf("ID: %d | %s | Âge: %d | Revenu: %.2f DH | Score: %d/100%n",
                p.getId(),
                p.getNomComplet(),
                p.getAge(),
                p.getRevenuMensuel(),
                p.getScore());
        }
    }

    private int lireEntier() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
