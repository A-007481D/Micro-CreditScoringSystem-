package com.microfin.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(FORMATTER) : "";
    }

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    public static int calculerAge(LocalDate dateNaissance) {
        return (int) ChronoUnit.YEARS.between(dateNaissance, LocalDate.now());
    }

    public static long joursEntre(LocalDate debut, LocalDate fin) {
        return ChronoUnit.DAYS.between(debut, fin);
    }

    public static int moisEntre(LocalDate debut, LocalDate fin) {
        return (int) ChronoUnit.MONTHS.between(debut, fin);
    }

    public static boolean estDateValide(String dateStr) {
        return parseDate(dateStr) != null;
    }
}
