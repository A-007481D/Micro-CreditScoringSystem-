package com.microfin.view;

import com.microfin.service.*;
import com.microfin.util.FormatUtil;

import java.util.Scanner;

public class MenuPrincipal {
    
    private final Scanner scanner;
    private final ClientService clientService;
    private final CreditService creditService;
    private final PaiementService paiementService;
    private final AnalyticsService analyticsService;
    
    private final MenuClient menuClient;
    private final MenuCredit menuCredit;
    private final MenuPaiement menuPaiement;
    private final MenuAnalytics menuAnalytics;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.clientService = new ClientService();
        this.creditService = new CreditService();
        this.paiementService = new PaiementService();
        this.analyticsService = new AnalyticsService();
        
        this.menuClient = new MenuClient(scanner, clientService);
        this.menuCredit = new MenuCredit(scanner, creditService, clientService);
        this.menuPaiement = new MenuPaiement(scanner, paiementService, creditService);
        this.menuAnalytics = new MenuAnalytics(scanner, analyticsService);
    }

    public void afficher() {
        boolean continuer = true;
        
        while (continuer) {
            FormatUtil.afficherTitre("Système de Scoring Crédit - Microfinance");
            System.out.println("1. Gestion des Clients");
            System.out.println("2. Gestion des Crédits");
            System.out.println("3. Gestion des Paiements");
            System.out.println("4. Analytics & Recherche");
            System.out.println("0. Quitter");
            FormatUtil.afficherSeparateur();
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1 -> menuClient.afficher();
                case 2 -> menuCredit.afficher();
                case 3 -> menuPaiement.afficher();
                case 4 -> menuAnalytics.afficher();
                case 0 -> {
                    System.out.println("\n✓ Au revoir!");
                    continuer = false;
                }
                default -> System.out.println("✗ Choix invalide!");
            }
        }
        
        scanner.close();
    }

    private int lireEntier() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
