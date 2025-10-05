package com.microfin.util;

public class ValidationUtil {
    
    public static boolean estNonVide(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean estPositif(double nombre) {
        return nombre > 0;
    }

    public static boolean estPositifOuZero(double nombre) {
        return nombre >= 0;
    }

    public static boolean estEntreMinMax(int valeur, int min, int max) {
        return valeur >= min && valeur <= max;
    }

    public static boolean estEmailValide(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static String validerNonVide(String valeur, String nomChamp) {
        if (!estNonVide(valeur)) {
            throw new IllegalArgumentException(nomChamp + " ne peut pas être vide");
        }
        return valeur.trim();
    }

    public static double validerPositif(double valeur, String nomChamp) {
        if (!estPositif(valeur)) {
            throw new IllegalArgumentException(nomChamp + " doit être positif");
        }
        return valeur;
    }
}
