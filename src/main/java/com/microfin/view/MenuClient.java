package com.microfin.view;

import com.microfin.enums.*;
import com.microfin.model.Employe;
import com.microfin.model.Personne;
import com.microfin.model.Professionnel;
import com.microfin.service.ClientService;
import com.microfin.util.DateUtil;
import com.microfin.util.FormatUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuClient {
    
    private final Scanner scanner;
    private final ClientService clientService;

    public MenuClient(Scanner scanner, ClientService clientService) {
        this.scanner = scanner;
        this.clientService = clientService;
    }

    public void afficher() {
        boolean continuer = true;
        
        while (continuer) {
            FormatUtil.afficherTitre("Gestion des Clients");
            System.out.println("1. Créer un client Employé");
            System.out.println("2. Créer un client Professionnel");
            System.out.println("3. Consulter un client");
            System.out.println("4. Modifier un client");
            System.out.println("5. Lister tous les clients");
            System.out.println("6. Supprimer un client");
            System.out.println("0. Retour");
            FormatUtil.afficherSeparateur();
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    creerEmploye();
                    break;
                case 2:
                    creerProfessionnel();
                    break;
                case 3:
                    consulterClient();
                    break;
                case 4:
                    modifierClient();
                    break;
                case 5:
                    listerClients();
                    break;
                case 6:
                    supprimerClient();
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

    private void creerEmploye() {
        FormatUtil.afficherTitre("Créer un Client Employé");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        
        System.out.print("Date de naissance (jj/mm/aaaa): ");
        LocalDate dateNaissance = DateUtil.parseDate(scanner.nextLine());
        
        System.out.print("Ville: ");
        String ville = scanner.nextLine();
        
        System.out.print("Nombre d'enfants: ");
        int enfants = lireEntier();
        
        System.out.print("Investissements (DH): ");
        double investissements = lireDouble();
        
        System.out.println("Situation familiale (1:CELIBATAIRE, 2:MARIE, 3:DIVORCE, 4:VEUF): ");
        SituationFamiliale situation = choisirSituationFamiliale();
        
        System.out.print("Salaire mensuel (DH): ");
        double salaire = lireDouble();
        
        System.out.print("Ancienneté (années): ");
        int anciennete = lireEntier();
        
        System.out.print("Poste: ");
        String poste = scanner.nextLine();
        
        System.out.println("Type contrat (1:CDI, 2:CDD, 3:STAGE, 4:FREELANCE): ");
        TypeContrat typeContrat = choisirTypeContrat();
        
        System.out.println("Secteur (1:PUBLIC, 2:GRANDE_ENTREPRISE, 3:PME): ");
        Secteur secteur = choisirSecteur();
        
        Employe employe = new Employe();
        employe.setNom(nom);
        employe.setPrenom(prenom);
        employe.setDateDeNaissance(dateNaissance);
        employe.setVille(ville);
        employe.setNombreEnfants(enfants);
        employe.setInvestissement(investissements);
        employe.setSituationFamiliale(situation);
        employe.setSalaire(salaire);
        employe.setAnciennete(anciennete);
        employe.setPoste(poste);
        employe.setTypeContrat(typeContrat);
        employe.setSecteur(secteur);
        employe.setClientExistant(false);
        
        Employe created = clientService.creerEmploye(employe);
        if (created != null) {
            System.out.println("\n✓ Client créé avec succès!");
            System.out.println("ID: " + created.getId());
            System.out.println("Score initial: " + FormatUtil.formatScore(created.getScore()));
        } else {
            System.out.println("✗ Erreur lors de la création");
        }
    }

    private void creerProfessionnel() {
        FormatUtil.afficherTitre("Créer un Client Professionnel");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        
        System.out.print("Date de naissance (jj/mm/aaaa): ");
        LocalDate dateNaissance = DateUtil.parseDate(scanner.nextLine());
        
        System.out.print("Ville: ");
        String ville = scanner.nextLine();
        
        System.out.print("Nombre d'enfants: ");
        int enfants = lireEntier();
        
        System.out.print("Investissements (DH): ");
        double investissements = lireDouble();
        
        System.out.println("Situation familiale (1:CELIBATAIRE, 2:MARIE, 3:DIVORCE, 4:VEUF): ");
        SituationFamiliale situation = choisirSituationFamiliale();
        
        System.out.print("Revenu annuel (DH): ");
        double revenu = lireDouble();
        
        System.out.print("Immatriculation fiscale: ");
        String immatriculation = scanner.nextLine();
        
        System.out.println("Secteur activité (1:AGRICULTURE, 2:SERVICE, 3:COMMERCE, 4:ARTISANAT, 5:INDUSTRIE): ");
        SecteurActivite secteurActivite = choisirSecteurActivite();
        
        System.out.print("Activité spécifique: ");
        String activite = scanner.nextLine();
        
        Professionnel pro = new Professionnel();
        pro.setNom(nom);
        pro.setPrenom(prenom);
        pro.setDateDeNaissance(dateNaissance);
        pro.setVille(ville);
        pro.setNombreEnfants(enfants);
        pro.setInvestissement(investissements);
        pro.setSituationFamiliale(situation);
        pro.setRevenu(revenu);
        pro.setImmatriculationFiscale(immatriculation);
        pro.setSecteurActivite(secteurActivite);
        pro.setActivite(activite);
        pro.setClientExistant(false);
        
        Professionnel created = clientService.creerProfessionnel(pro);
        if (created != null) {
            System.out.println("\n✓ Client créé avec succès!");
            System.out.println("ID: " + created.getId());
            System.out.println("Score initial: " + FormatUtil.formatScore(created.getScore()));
        } else {
            System.out.println("✗ Erreur lors de la création");
        }
    }

    private void consulterClient() {
        System.out.print("Type client (EMPLOYE/PROFESSIONNEL): ");
        String type = scanner.nextLine();
        
        System.out.print("ID du client: ");
        Long id = lireLong();
        
        Optional<Personne> personneOpt = clientService.consulterClient(type, id);
        
        if (personneOpt.isPresent()) {
            Personne p = personneOpt.get();
            
            FormatUtil.afficherTitre("Profil Client");
            System.out.println("ID: " + p.getId());
            System.out.println("Nom complet: " + p.getNomComplet());
            System.out.println("Date naissance: " + DateUtil.formatDate(p.getDateDeNaissance()));
            System.out.println("Âge: " + p.getAge() + " ans");
            System.out.println("Ville: " + p.getVille());
            System.out.println("Enfants: " + p.getNombreEnfants());
            System.out.println("Investissement: " + FormatUtil.formatMoney(p.getInvestissement()));
            System.out.println("Situation familiale: " + p.getSituationFamiliale().getDescription());
            System.out.println("Score: " + FormatUtil.formatScore(p.getScore()));
            System.out.println("Client existant: " + (p.isClientExistant() ? "Oui" : "Non"));
            System.out.println("Revenu mensuel: " + FormatUtil.formatMoney(p.getRevenuMensuel()));
            
            if (p instanceof Employe) {
                Employe e = (Employe) p;
                System.out.println("\n--- Informations Employé ---");
                System.out.println("Salaire: " + FormatUtil.formatMoney(e.getSalaire()));
                System.out.println("Ancienneté: " + e.getAnciennete() + " ans");
                System.out.println("Poste: " + e.getPoste());
                System.out.println("Type contrat: " + e.getTypeContrat().getDescription());
                System.out.println("Secteur: " + e.getSecteur().getDescription());
            } else if (p instanceof Professionnel) {
                Professionnel pro = (Professionnel) p;
                System.out.println("\n--- Informations Professionnel ---");
                System.out.println("Revenu annuel: " + FormatUtil.formatMoney(pro.getRevenu()));
                System.out.println("Immatriculation: " + pro.getImmatriculationFiscale());
                System.out.println("Secteur: " + pro.getSecteurActivite().getDescription());
                System.out.println("Activité: " + pro.getActivite());
            }
        } else {
            System.out.println("✗ Client non trouvé");
        }
    }

    private void modifierClient() {
        System.out.print("Type client (EMPLOYE/PROFESSIONNEL): ");
        String type = scanner.nextLine();
        
        System.out.print("ID du client: ");
        Long id = lireLong();
        
        Optional<Personne> personneOpt = clientService.consulterClient(type, id);
        
        if (personneOpt.isEmpty()) {
            System.out.println("✗ Client non trouvé");
            return;
        }
        
        Personne p = personneOpt.get();
        
        System.out.println("Modification du client: " + p.getNomComplet());
        System.out.println("Laissez vide pour conserver la valeur actuelle");
        
        System.out.print("Nouveau salaire/revenu (actuel: " + FormatUtil.formatMoney(p.getRevenuMensuel()) + "): ");
        String revenuStr = scanner.nextLine();
        if (!revenuStr.isEmpty()) {
            double nouveauRevenu = Double.parseDouble(revenuStr);
            if (p instanceof Employe) {
                ((Employe) p).setSalaire(nouveauRevenu);
            } else if (p instanceof Professionnel) {
                ((Professionnel) p).setRevenu(nouveauRevenu * 12);
            }
        }
        
        System.out.print("Nouvelle ville (actuelle: " + p.getVille() + "): ");
        String ville = scanner.nextLine();
        if (!ville.isEmpty()) {
            p.setVille(ville);
        }
        
        boolean updated = false;
        if (type.equalsIgnoreCase("EMPLOYE")) {
            updated = clientService.modifierEmploye((Employe) p);
        } else {
            updated = clientService.modifierProfessionnel((Professionnel) p);
        }
        if (updated) {
            System.out.println("✓ Client modifié avec succès!");
            System.out.println("Nouveau score: " + FormatUtil.formatScore(p.getScore()));
        } else {
            System.out.println("✗ Erreur lors de la modification");
        }
    }

    private void listerClients() {
        List<Personne> clients = clientService.listerTousLesClients();
        
        FormatUtil.afficherTitre("Liste des Clients (" + clients.size() + ")");
        
        for (Personne p : clients) {
            String type = p instanceof Employe ? "EMP" : "PRO";
            System.out.printf("[%s] ID: %d | %s | Âge: %d | Score: %d | Revenu: %s%n",
                type,
                p.getId(),
                p.getNomComplet(),
                p.getAge(),
                p.getScore(),
                FormatUtil.formatMoney(p.getRevenuMensuel()));
        }
    }

    private void supprimerClient() {
        System.out.print("Type client (EMPLOYE/PROFESSIONNEL): ");
        String type = scanner.nextLine();
        
        System.out.print("ID du client: ");
        Long id = lireLong();
        
        System.out.print("Êtes-vous sûr? (oui/non): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("oui")) {
            boolean deleted = clientService.supprimerClient(type, id);
            if (deleted) {
                System.out.println("✓ Client supprimé");
            } else {
                System.out.println("✗ Erreur lors de la suppression");
            }
        } else {
            System.out.println("Suppression annulée");
        }
    }

    private SituationFamiliale choisirSituationFamiliale() {
        int choix = lireEntier();
        switch (choix) {
            case 1:
                return SituationFamiliale.CELIBATAIRE;
            case 2:
                return SituationFamiliale.MARIE;
            case 3:
                return SituationFamiliale.DIVORCE;
            case 4:
                return SituationFamiliale.VEUF;
            default:
                return SituationFamiliale.CELIBATAIRE;
        }
    }

    private TypeContrat choisirTypeContrat() {
        int choix = lireEntier();
        switch (choix) {
            case 1:
                return TypeContrat.CDI;
            case 2:
                return TypeContrat.CDD;
            case 3:
                return TypeContrat.CDD;
            case 4:
                return TypeContrat.CDI;
            default:
                return TypeContrat.CDI;
        }
    }

    private Secteur choisirSecteur() {
        int choix = lireEntier();
        switch (choix) {
            case 1:
                return Secteur.PUBLIC;
            case 2:
                return Secteur.GRANDE_ENTREPRISE;
            case 3:
                return Secteur.PME;
            default:
                return Secteur.PUBLIC;
        }
    }

    private SecteurActivite choisirSecteurActivite() {
        int choix = lireEntier();
        switch (choix) {
            case 1:
                return SecteurActivite.AGRICULTURE;
            case 2:
                return SecteurActivite.SERVICE;
            case 3:
                return SecteurActivite.COMMERCE;
            case 4:
                return SecteurActivite.ARTISANAT;
            case 5:
                return SecteurActivite.INDUSTRIE;
            default:
                return SecteurActivite.COMMERCE;
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
