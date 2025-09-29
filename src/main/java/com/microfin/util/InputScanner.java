package main.java.com.microfin.util;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InputScanner {
    public static InputScanner instance;
    private final Scanner scanner;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    private InputScanner() {
        this.scanner = new Scanner(System.in);
    }

    public static synchronized InputScanner getInstance() {
        if (instance == null ) {
            instance = new InputScanner();
        }
        return instance;
    }

}
