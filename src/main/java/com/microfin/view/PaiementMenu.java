package main.java.com.microfin.view;


import java.util.Scanner;

public class PaiementMenu {
    public static final Scanner sc = new Scanner(System.in);
    public static void showPaiementMenu() {
        while(true) {
            System.out.println("\n###----------- Gestion des Payments  -----------###");
            System.out.println("1. Enregistrer un paiement\n");
            System.out.println("2. Consulter échéancier d'un crédit \n");
            System.out.println("3. Lister échéances impayées\n");
            System.out.println("4. Régulariser un impayé\n");
            System.out.println("5. Lister incidents par client\n");
            System.out.println("6. Mettre à jour statuts automatiquementt\n");
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

}
