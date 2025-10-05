package com.microfin.view;

import com.microfin.model.Echeance;
import com.microfin.model.Incident;
import com.microfin.service.CreditService;
import com.microfin.service.PaiementService;
import com.microfin.util.DateUtil;
import com.microfin.util.FormatUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuPaiement {
    
    private final Scanner scanner;
    private final PaiementService paiementService;
    private final CreditService creditService;

    public MenuPaiement(Scanner scanner, PaiementService paiementService, CreditService creditService) {
        this.scanner = scanner;
        this.paiementService = paiementService;
        this.creditService = creditService;
    }

    public void afficher() {
        boolean continuer = true;
        
        while (continuer) {
            FormatUtil.afficherTitre("Gestion des Paiements");
            System.out.println("1. Enregistrer un paiement");
            System.out.println("2. Voir échéances en retard");
            System.out.println("3. Voir échéances impayées");
            System.out.println("4. Consulter historique incidents");
            System.out.println("5. Voir incidents récents");
            System.out.println("0. Retour");
            FormatUtil.afficherSeparateur();
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    enregistrerPaiement();
                    break;
                case 2:
                    voirEcheancesEnRetard();
                    break;
                case 3:
                    voirEcheancesImpayees();
                    break;
                case 4:
                    consulterHistorique();
                    break;
                case 5:
                    voirIncidentsRecents();
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

    private void enregistrerPaiement() {
        FormatUtil.afficherTitre("ENREGISTREMENT PAIEMENT");
        
        // Demander l'ID du crédit
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        if (creditId <= 0) {
            System.err.println("✗ ID de crédit invalide");
            return;
        }
        
        // Afficher les échéances non payées
        List<Echeance> echeancesAPayer = paiementService.listerEcheancesAPayer(creditId);
        List<Echeance> echeancesEnRetard = paiementService.listerEcheancesEnRetard(creditId);
        
        // Afficher d'abord les échéances à payer (non échues ou peu de retard)
        System.out.println("\n=== ÉCHÉANCES À PAYER ===");
        if (echeancesAPayer.isEmpty()) {
            System.out.println("Aucune échéance à payer pour le moment.");
        } else {
            for (Echeance e : echeancesAPayer) {
                System.out.printf("ID: %d | N°%d | Date: %s | Montant: %s | Statut: %s%n",
                    e.getId(),
                    e.getNumeroEcheance(),
                    DateUtil.formatDate(e.getDateEcheance()),
                    FormatUtil.formatMoney(e.getMensualite()),
                    e.determinerStatut().toString().replace("_", " ").toLowerCase());
            }
        }
        
        // Ensuite afficher les échéances en retard
        System.out.println("\n=== ÉCHÉANCES EN RETARD ===");
        if (echeancesEnRetard.isEmpty()) {
            System.out.println("Aucune échéance en retard.");
        } else {
            for (Echeance e : echeancesEnRetard) {
                long joursRetard = DateUtil.joursEntre(e.getDateEcheance(), LocalDate.now());
                System.out.printf("ID: %d | N°%d | Date: %s | Jours de retard: %d | Montant: %s%n",
                    e.getId(),
                    e.getNumeroEcheance(),
                    DateUtil.formatDate(e.getDateEcheance()),
                    joursRetard,
                    FormatUtil.formatMoney(e.getMensualite()));
            }
        }
        
        // Demander l'ID de l'échéance à payer
        System.out.print("\nID de l'échéance à payer (0 pour annuler): ");
        Long echeanceId = lireLong();
        
        if (echeanceId <= 0) {
            System.out.println("Opération annulée.");
            return;
        }
        
        // Vérifier que l'échéance existe
        boolean echeanceValide = false;
        Echeance echeanceSelectionnee = null;
        
        // Vérifier dans les échéances à payer
        for (Echeance e : echeancesAPayer) {
            if (e.getId().equals(echeanceId)) {
                echeanceValide = true;
                echeanceSelectionnee = e;
                break;
            }
        }
        
        // Si pas trouvée, vérifier dans les échéances en retard
        if (!echeanceValide) {
            for (Echeance e : echeancesEnRetard) {
                if (e.getId().equals(echeanceId)) {
                    echeanceValide = true;
                    echeanceSelectionnee = e;
                    break;
                }
            }
        }
        
        if (!echeanceValide || echeanceSelectionnee == null) {
            System.err.println("✗ Aucune échéance valide trouvée avec cet ID");
            return;
        }
        
        // Vérifier si l'échéance est déjà payée
        if (echeanceSelectionnee.getDateDePaiement() != null) {
            System.err.println("✗ Cette échéance a déjà été payée le " + 
                DateUtil.formatDate(echeanceSelectionnee.getDateDePaiement()));
            return;
        }
        
        // Demander la date de paiement
        LocalDate datePaiement = null;
        while (datePaiement == null) {
            System.out.print("Date de paiement (jj/mm/aaaa) ou 'aujourd'hui' pour " + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ": ");
            String dateStr = scanner.nextLine().trim();
            
            if (dateStr.equalsIgnoreCase("aujourd'hui") || dateStr.isEmpty()) {
                datePaiement = LocalDate.now();
            } else {
                try {
                    datePaiement = DateUtil.parseDate(dateStr);
                    if (datePaiement.isAfter(LocalDate.now())) {
                        System.err.println("✗ La date de paiement ne peut pas être dans le futur");
                        datePaiement = null;
                    }
                } catch (Exception e) {
                    System.err.println("✗ Format de date invalide. Utilisez le format jj/mm/aaaa");
                }
            }
        }
        
        // Demander l'ID du client
        System.out.print("ID du client: ");
        Long clientId = lireLong();
        
        if (clientId <= 0) {
            System.err.println("✗ ID de client invalide");
            return;
        }
        
        // Demander le type de client
        String typeClient = "";
        while (!typeClient.equalsIgnoreCase("EMPLOYE") && !typeClient.equalsIgnoreCase("PROFESSIONNEL")) {
            System.out.print("Type client (EMPLOYE/PROFESSIONNEL): ");
            typeClient = scanner.nextLine().trim().toUpperCase();
            
            if (typeClient.isEmpty()) {
                typeClient = "EMPLOYE"; // Valeur par défaut
                break;
            }
        }
        
        // Enregistrer le paiement
        System.out.println("\nRécapitulatif du paiement:");
        System.out.println("Échéance N°" + echeanceSelectionnee.getNumeroEcheance());
        System.out.println("Date d'échéance: " + DateUtil.formatDate(echeanceSelectionnee.getDateEcheance()));
        System.out.println("Montant: " + FormatUtil.formatMoney(echeanceSelectionnee.getMensualite()));
        System.out.println("Date de paiement: " + DateUtil.formatDate(datePaiement));
        
        System.out.print("\nConfirmer le paiement (O/N)? ");
        String confirmation = scanner.nextLine().trim().toUpperCase();
        
        if (confirmation.equals("O") || confirmation.equals("OUI")) {
            if (paiementService.enregistrerPaiement(echeanceId, datePaiement, clientId, typeClient)) {
                System.out.println("\n✓ Paiement enregistré avec succès!");
                
                // Afficher le statut mis à jour
                Echeance echeanceMiseAJour = creditService.listerEcheances(creditId).stream()
                    .filter(e -> e.getId().equals(echeanceId))
                    .findFirst()
                    .orElse(null);
                
                if (echeanceMiseAJour != null) {
                    System.out.println("Nouveau statut: " + 
                        echeanceMiseAJour.getStatutPaiement().toString().replace("_", " ").toLowerCase());
                }
            } else {
                System.err.println("\n✗ Erreur lors de l'enregistrement du paiement");
            }
        } else {
            System.out.println("Paiement annulé.");
        }
    }

    private void voirEcheancesEnRetard() {
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        if (creditId <= 0) {
            System.err.println("✗ ID de crédit invalide");
            return;
        }
        
        List<Echeance> retards = paiementService.listerEcheancesEnRetard(creditId);
        
        FormatUtil.afficherTitre("ÉCHÉANCES EN RETARD (" + retards.size() + ")");
        
        if (retards.isEmpty()) {
            System.out.println("Aucune échéance en retard pour ce crédit.");
            return;
        }
        
        // Afficher un résumé
        double totalDus = retards.stream()
            .mapToDouble(Echeance::getMensualite)
            .sum();
            
        System.out.printf("Total dû: %s%n%n", FormatUtil.formatMoney(totalDus));
        
        // Afficher le tableau des échéances
        System.out.println("┌───────┬────────────┬───────────────┬────────────────┐");
        System.out.println("│ N°    │ Date       │ Jours retard  │ Montant        │");
        System.out.println("├───────┼────────────┼───────────────┼────────────────┤");
        
        for (Echeance e : retards) {
            long joursRetard = DateUtil.joursEntre(e.getDateEcheance(), LocalDate.now());
            System.out.printf("│ %-5d │ %-10s │ %-13d │ %14s │%n",
                e.getNumeroEcheance(),
                DateUtil.formatDate(e.getDateEcheance()),
                joursRetard,
                FormatUtil.formatMoney(e.getMensualite()));
        }
        
        System.out.println("└───────┴────────────┴───────────────┴────────────────┘");
        
        // Proposer de faire un paiement
        System.out.print("\nSouhaitez-vous enregistrer un paiement (O/N)? ");
        String reponse = scanner.nextLine().trim().toUpperCase();
        
        if (reponse.equals("O") || reponse.equals("OUI")) {
            enregistrerPaiement();
        }
    }

    private void voirEcheancesImpayees() {
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        if (creditId <= 0) {
            System.err.println("✗ ID de crédit invalide");
            return;
        }
        
        List<Echeance> impayes = paiementService.listerEcheancesImpayees(creditId);
        
        FormatUtil.afficherTitre("ÉCHÉANCES IMPAYÉES (" + impayes.size() + ")");
        
        if (impayes.isEmpty()) {
            System.out.println("Aucune échéance impayée pour ce crédit.");
            return;
        }
        
        // Afficher un résumé
        double totalDus = impayes.stream()
            .mapToDouble(Echeance::getMensualite)
            .sum();
            
        System.out.printf("Total impayé: %s%n%n", FormatUtil.formatMoney(totalDus));
        
        // Afficher le tableau des échéances
        System.out.println("┌───────┬────────────┬───────────────┬────────────────┐");
        System.out.println("│ N°    │ Date       │ Jours écoulés │ Montant        │");
        System.out.println("├───────┼────────────┼───────────────┼────────────────┤");
        
        for (Echeance e : impayes) {
            long joursEcoules = DateUtil.joursEntre(e.getDateEcheance(), LocalDate.now());
            System.out.printf("│ %-5d │ %-10s │ %-13d │ %14s │%n",
                e.getNumeroEcheance(),
                DateUtil.formatDate(e.getDateEcheance()),
                joursEcoules,
                FormatUtil.formatMoney(e.getMensualite()));
        }
        
        System.out.println("└───────┴────────────┴───────────────┴────────────────┘");
        
        // Proposer de faire un paiement
        System.out.print("\nSouhaitez-vous enregistrer un paiement (O/N)? ");
        String reponse = scanner.nextLine().trim().toUpperCase();
        
        if (reponse.equals("O") || reponse.equals("OUI")) {
            enregistrerPaiement();
        }
    }

    private void consulterHistorique() {
        System.out.print("ID du client: ");
        Long clientId = lireLong();
        
        List<Incident> incidents = paiementService.consulterHistoriqueIncidents(clientId);
        
        FormatUtil.afficherTitre("Historique Incidents (" + incidents.size() + ")");
        
        for (Incident i : incidents) {
            System.out.printf("Date: %s | Type: %s | Impact: %d pts%n",
                DateUtil.formatDate(i.getDateIncident()),
                i.getTypeIncident().getDescription(),
                i.getScore());
        }
    }

    private void voirIncidentsRecents() {
        System.out.print("ID du client: ");
        Long clientId = lireLong();
        
        System.out.print("Nombre de mois: ");
        int mois = lireEntier();
        
        List<Incident> incidents = paiementService.consulterIncidentsRecents(clientId, mois);
        
        FormatUtil.afficherTitre("Incidents Récents (" + incidents.size() + ")");
        
        for (Incident i : incidents) {
            System.out.printf("Date: %s | Type: %s | Impact: %d pts%n",
                DateUtil.formatDate(i.getDateIncident()),
                i.getTypeIncident().getDescription(),
                i.getScore());
        }
    }

    private int lireEntier() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private long lireLong() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }
}
