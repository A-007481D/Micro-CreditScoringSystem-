package com.microfin.view;

import com.microfin.enums.TypeCredit;
import com.microfin.model.Credit;
import com.microfin.model.Echeance;
import com.microfin.model.Personne;
import com.microfin.service.ClientService;
import com.microfin.service.CreditService;
import com.microfin.util.DateUtil;
import com.microfin.util.FormatUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuCredit {
    
    private final Scanner scanner;
    private final CreditService creditService;
    private final ClientService clientService;

    public MenuCredit(Scanner scanner, CreditService creditService, ClientService clientService) {
        this.scanner = scanner;
        this.creditService = creditService;
        this.clientService = clientService;
    }

    public void afficher() {
        boolean continuer = true;
        
        while (continuer) {
            FormatUtil.afficherTitre("Gestion des Crédits");
            System.out.println("1. Demander un crédit");
            System.out.println("2. Consulter un crédit");
            System.out.println("3. Lister crédits d'un client");
            System.out.println("4. Voir échéances d'un crédit");
            System.out.println("5. Clôturer un crédit");
            System.out.println("0. Retour");
            FormatUtil.afficherSeparateur();
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    demanderCredit();
                    break;
                case 2:
                    consulterCredit();
                    break;
                case 3:
                    listerCreditsClient();
                    break;
                case 4:
                    voirEcheances();
                    break;
                case 5:
                    cloturerCredit();
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

    private void demanderCredit() {
        FormatUtil.afficherTitre("Demande de Crédit");
        
        System.out.print("Type client (EMPLOYE/PROFESSIONNEL): ");
        String typeClient = scanner.nextLine();
        
        System.out.print("ID du client: ");
        Long clientId = lireLong();
        
        Optional<Personne> personneOpt = clientService.consulterClient(typeClient, clientId);
        
        if (personneOpt.isEmpty()) {
            System.out.println("✗ Client non trouvé");
            return;
        }
        
        Personne personne = personneOpt.get();
        
        System.out.println("\nClient: " + personne.getNomComplet());
        System.out.println("Score actuel: " + FormatUtil.formatScore(personne.getScore()));
        System.out.println("Revenu: " + FormatUtil.formatMoney(personne.getRevenuMensuel()));
        
        System.out.print("\nMontant demandé: ");
        double montant = lireDouble();
        
        System.out.print("Durée (en mois): ");
        int duree = lireEntier();
        
        System.out.println("Type crédit (1:IMMOBILIER, 2:CONSOMMATION, 3:PERSONNEL, 4:AUTO, 5:EQUIPEMENT, 6:PROFESSIONNEL): ");
        TypeCredit typeCredit = choisirTypeCredit();
        
        Credit credit = creditService.demanderCredit(personne, typeClient, montant, duree, typeCredit);
        
        if (credit != null) {
            System.out.println("\n✓ Demande traitée!");
            System.out.println("Décision: " + credit.getDecision().getDescription());
            System.out.println("Montant octroyé: " + FormatUtil.formatMoney(credit.getMontantOctroye()));
            System.out.println("Taux d'intérêt: " + FormatUtil.formatPercent(credit.getTauxInteret()));
            System.out.println("Mensualité: " + FormatUtil.formatMoney(credit.calculerMensualite()));
        } else {
            System.out.println("✗ Erreur lors du traitement");
        }
    }

    private void consulterCredit() {
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        Optional<Credit> creditOpt = creditService.consulterCredit(creditId);
        
        if (creditOpt.isEmpty()) {
            System.out.println("✗ Crédit non trouvé");
            return;
        }
        
        Credit credit = creditOpt.get();
        
        FormatUtil.afficherTitre("Détails Crédit");
        System.out.println("ID: " + credit.getId());
        System.out.println("Client ID: " + credit.getClientId());
        System.out.println("Date: " + DateUtil.formatDate(credit.getDateDeCredit()));
        System.out.println("Montant demandé: " + FormatUtil.formatMoney(credit.getMontantDemande()));
        System.out.println("Montant octroyé: " + FormatUtil.formatMoney(credit.getMontantOctroye()));
        System.out.println("Taux: " + FormatUtil.formatPercent(credit.getTauxInteret()));
        System.out.println("Durée: " + credit.getDureeEnMois() + " mois");
        System.out.println("Type: " + credit.getTypeCredit().getDescription());
        System.out.println("Décision: " + credit.getDecision().getDescription());
        System.out.println("Score au moment: " + FormatUtil.formatScore(credit.getScoreAuMomentDuCredit()));
        System.out.println("Mensualité: " + FormatUtil.formatMoney(credit.calculerMensualite()));
        System.out.println("Actif: " + (credit.isActif() ? "Oui" : "Non"));
    }

    private void listerCreditsClient() {
        System.out.print("ID du client: ");
        Long clientId = lireLong();
        
        List<Credit> credits = creditService.listerCreditsClient(clientId);
        
        FormatUtil.afficherTitre("Crédits du Client (" + credits.size() + ")");
        
        for (Credit c : credits) {
            System.out.printf("ID: %d | Date: %s | Montant: %s | Décision: %s | Actif: %s%n",
                c.getId(),
                DateUtil.formatDate(c.getDateDeCredit()),
                FormatUtil.formatMoney(c.getMontantOctroye()),
                c.getDecision().name(),
                c.isActif() ? "Oui" : "Non");
        }
    }

    private void voirEcheances() {
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        List<Echeance> echeances = creditService.listerEcheances(creditId);
        
        FormatUtil.afficherTitre("Échéances (" + echeances.size() + ")");
        
        for (Echeance e : echeances) {
            String datePaiement = e.getDateDePaiement() != null ? 
                DateUtil.formatDate(e.getDateDePaiement()) : "Non payé";
            
            System.out.printf("N°%d | Échéance: %s | Montant: %s | Payé: %s | Statut: %s%n",
                e.getNumeroEcheance(),
                DateUtil.formatDate(e.getDateEcheance()),
                FormatUtil.formatMoney(e.getMensualite()),
                datePaiement,
                e.getStatutPaiement().getDescription());
        }
    }

    private void cloturerCredit() {
        System.out.print("ID du crédit: ");
        Long creditId = lireLong();
        
        if (creditService.cloturerCredit(creditId)) {
            System.out.println("✓ Crédit clôturé");
        } else {
            System.out.println("✗ Erreur");
        }
    }

    private TypeCredit choisirTypeCredit() {
        int choix = lireEntier();
        switch (choix) {
            case 1:
                return TypeCredit.IMMOBILIER;
            case 2:
                return TypeCredit.CONSOMMATION;
            case 3:
                return TypeCredit.PERSONNEL;
            case 4:
                return TypeCredit.AUTO;
            case 5:
                return TypeCredit.EQUIPEMENT;
            case 6:
                return TypeCredit.PROFESSIONNEL;
            default:
                return TypeCredit.PERSONNEL;
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

    private double lireDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
