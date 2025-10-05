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
                case 1 -> enregistrerPaiement();
                case 2 -> voirEcheancesEnRetard();
                case 3 -> voirEcheancesImpayees();
                case 4 -> consulterHistorique();
                case 5 -> voirIncidentsRecents();
                case 0 -> continuer = false;
                default -> System.out.println("✗ Choix invalide!");
            }
        }
    }

    private void enregistrerPaiement() {
        FormatUtil.afficherTitre("Enregistrement Paiement");
        
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        List<Echeance> echeances = creditService.listerEcheances(creditId);
        
        System.out.println("\nÉchéances non payées:");
        for (Echeance e : echeances) {
            if (e.getDateDePaiement() == null) {
                System.out.printf("ID: %d | N°%d | Date: %s | Montant: %s%n",
                    e.getId(),
                    e.getNumeroEcheance(),
                    DateUtil.formatDate(e.getDateEcheance()),
                    FormatUtil.formatMoney(e.getMensualite()));
            }
        }
        
        System.out.print("\nID de l'échéance: ");
        Long echeanceId = lireLong();
        
        System.out.print("Date de paiement (jj/mm/aaaa) ou 'aujourd'hui': ");
        String dateStr = scanner.nextLine();
        LocalDate datePaiement = dateStr.equalsIgnoreCase("aujourd'hui") ? 
            LocalDate.now() : DateUtil.parseDate(dateStr);
        
        System.out.print("ID du client: ");
        Long clientId = lireLong();
        
        System.out.print("Type client (EMPLOYE/PROFESSIONNEL): ");
        String typeClient = scanner.nextLine();
        
        if (paiementService.enregistrerPaiement(echeanceId, datePaiement, clientId, typeClient)) {
            System.out.println("✓ Paiement enregistré avec succès!");
        } else {
            System.out.println("✗ Erreur lors de l'enregistrement");
        }
    }

    private void voirEcheancesEnRetard() {
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        List<Echeance> retards = paiementService.listerEcheancesEnRetard(creditId);
        
        FormatUtil.afficherTitre("Échéances en Retard (" + retards.size() + ")");
        
        for (Echeance e : retards) {
            System.out.printf("N°%d | Date: %s | Montant: %s | Jours retard: %d%n",
                e.getNumeroEcheance(),
                DateUtil.formatDate(e.getDateEcheance()),
                FormatUtil.formatMoney(e.getMensualite()),
                DateUtil.joursEntre(e.getDateEcheance(), LocalDate.now()));
        }
    }

    private void voirEcheancesImpayees() {
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        List<Echeance> impayes = paiementService.listerEcheancesImpayees(creditId);
        
        FormatUtil.afficherTitre("Échéances Impayées (" + impayes.size() + ")");
        
        for (Echeance e : impayes) {
            System.out.printf("N°%d | Date: %s | Montant: %s | Jours: %d%n",
                e.getNumeroEcheance(),
                DateUtil.formatDate(e.getDateEcheance()),
                FormatUtil.formatMoney(e.getMensualite()),
                DateUtil.joursEntre(e.getDateEcheance(), LocalDate.now()));
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
