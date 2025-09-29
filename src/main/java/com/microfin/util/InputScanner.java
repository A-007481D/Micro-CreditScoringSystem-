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

    public String readString(String prompt){
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(int prompt){
        int value;
        value = Integer.parseInt(scanner.nextLine().trim());
        return value;
    }

    public double readDouble(double prompt){
        double value;
        value = Double.parseDouble(scanner.nextLine().trim());
        return value;
    }


    public void close() {
        if(scanner != null) {
            scanner.close();
        }
    }

}
