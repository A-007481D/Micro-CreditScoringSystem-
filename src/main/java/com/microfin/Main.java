package com.microfin;

import com.microfin.config.DBConnection;
import com.microfin.view.MenuPrincipal;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE SCORING CRÉDIT - MICROFINANCE MAROC              ║");
        System.out.println("║   Version 1.0                                                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        DBConnection.getInstance();
        
        MenuPrincipal menuPrincipal = new MenuPrincipal();
        menuPrincipal.afficher();
        
        DBConnection.getInstance().closeConnection();
    }
}
