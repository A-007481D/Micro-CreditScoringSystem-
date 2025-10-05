package com.microfin.util;

import java.text.DecimalFormat;

public class FormatUtil {
    
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("0.00");

    public static String formatMoney(double montant) {
        return MONEY_FORMAT.format(montant) + " DH";
    }

    public static String formatPercent(double pourcentage) {
        return PERCENT_FORMAT.format(pourcentage) + "%";
    }

    public static String formatScore(int score) {
        return score + "/100";
    }

    public static void afficherLigne() {
        System.out.println("═".repeat(80));
    }

    public static void afficherSeparateur() {
        System.out.println("-".repeat(80));
    }

    public static void afficherTitre(String titre) {
        afficherLigne();
        System.out.println("  " + titre.toUpperCase());
        afficherLigne();
    }
}
