package main.java.com.microfin.view;

import java.util.Scanner;

public class MainMenu {

    private static final Scanner sc = new Scanner(System.in);

    public static void showMainMenu() {
        while(true) {
            System.out.println("\n###----------- ANCHOR-MFI Console -----------###");
            System.out.println("1. Gestion des clients\n");
            System.out.println("2. Gestion des Credits\n");
            System.out.println("3. Gestion des Paiements\n");
            System.out.println("5. Analytics & Rapports\n");
            System.out.println("6. Historique & Audit\n");
            System.out.println("0. Quitter\n");
            System.out.println("Choix :");
            String choice = sc.nextLine();
            switch(choice) {

                case "0" :
                    System.out.println("Thella"); return;
                default:
                    System.out.println("Choix invalide, 3awd khtar");

            }
        }
    }


}