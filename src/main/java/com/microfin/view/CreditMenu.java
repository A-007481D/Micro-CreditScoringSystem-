package main.java.com.microfin.view;

import java.util.Scanner;

public class CreditMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void showCreditMenu() {
        while(true) {
            System.out.println("\n###----------- Gestion des Credits  -----------###");
            System.out.println("1. Demander un nouveau crédit\n");
            System.out.println("2. Consulter un crédit\n");
            System.out.println("3. Lister crédits par client\n");
            System.out.println("4. Supprimer un client\n");
            System.out.println("5. Lister tous les crédits en cours\n");
            System.out.println("6. Simuler capacité d'emprunt\n");
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
