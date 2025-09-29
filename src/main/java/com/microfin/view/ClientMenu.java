package main.java.com.microfin.view;

import java.util.Scanner;

public class ClientMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void showClientMenu() {
        while(true) {
            System.out.println("\n###----------- Clients Management -----------###");
            System.out.println("1. Créer un nouveau client\n");
            System.out.println("2. Modifier un client\n");
            System.out.println("3. Consulter le profil d'un client\n");
            System.out.println("4. Supprimer un client\n");
            System.out.println("5. Lister tous les clients\n");
            System.out.println("6. Rechercher un client\n");
            System.out.println("0. Retour au menu principal\n");
            System.out.println("Choix :");
            String choice = sc.nextLine();
            switch(choice) {
                case "0" :
                    System.out.println("Thella");
                default:
                    System.out.println("choix invalid, 3awd khtar");
            }
        }
    }
}
