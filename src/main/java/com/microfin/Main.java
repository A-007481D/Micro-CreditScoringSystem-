package com.microfin;

import com.microfin.config.DBConnection;
import com.microfin.view.MenuPrincipal;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE SCORING CRÉDIT - MICROFINANCE MAROC               ║");
        System.out.println("║   Version 1.0 (Java 8)                                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            // Initialize database connection
            DBConnection dbConnection = DBConnection.getInstance();
            
            // Start the application
            MenuPrincipal menuPrincipal = new MenuPrincipal();
            menuPrincipal.afficher();
            
            // Close the connection when done
            dbConnection.closeConnection();
        } catch (Exception e) {
            System.err.println("Une erreur critique est survenue: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
